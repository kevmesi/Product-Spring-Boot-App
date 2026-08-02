package com.project.demo.rest;

import com.project.demo.entity.Product;
import com.project.demo.service.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Covered By ProductRestControllerTest.
 */
@RestController
@RequestMapping("/api/products")
@Validated
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    // expose "/products" and get list of products
    @GetMapping()
    public List<Product> findAllProducts(){
        return productService.findAll();
    }

    /**
     * Returns product for given code
     * @param code unique 10-character code of the product
     * @throws ResourceNotFoundException if there is no product for given code
     */
    @GetMapping("{code}")
    public Product findProductByCode(@PathVariable String code) throws ResourceNotFoundException {

        Product product = productService.findByCode(code);

        if (product == null){
            throw new ResourceNotFoundException("Product not found, code: " + code);
        }
        return product;
    }

    // expose "/available" and get list of all available products
    @GetMapping("/available")
    public List<Product> findAllAvailableProducts(){
        return productService.findAvailable();
    }

    /**
     * Saves new product.
     * @param product product to be saved without saved set id and without save price_usd
     * @param result errors from validations
     * @return saved product
     * @throws BadRequestException thrown if validations are not passed
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@RequestBody @Valid Product product,
                        BindingResult result) throws BadRequestException {

        Product savedProduct = productService.save(product);

        if (result.hasErrors()){
            throw new BadRequestException(result.getAllErrors().getFirst().getDefaultMessage());
        }

        return savedProduct;
    }
}
