package com.project.demo.rest;

import com.project.demo.entity.Product;
import com.project.demo.service.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    // add an init binder to trim input strings
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    // expose "/products" and get list of products
    @GetMapping("/products")
    public List<Product> findAllProducts(){
        return productService.findAll();
    }

    @GetMapping("/products/{code}")
    public Product findProductByCode(@PathVariable String code){
        Product product = productService.findByCode(code);

        if (product == null){
            throw new ResourceNotFoundException("Product not found, code:" + code);
        }
        System.out.println("Product: " + product);
        return product;
    }

    @GetMapping("/available")
    public List<Product> findAllAvailableProducts(){
        return productService.findAvailable();
    }

    @PostMapping("/products")
    public Product save(@RequestBody @Valid Product product,
                        BindingResult result) throws Exception {

        Product savedProduct = productService.save(product);

        if (result.hasErrors()){
            throw new BadRequestException(result.getAllErrors().getFirst().getDefaultMessage());
        }

        return savedProduct;
    }
}
