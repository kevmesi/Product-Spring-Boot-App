package com.project.demo.rest;

import com.project.demo.entity.Product;
import com.project.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
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
     * @return saved product
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@RequestBody @Valid Product product) {
        return productService.save(product);
    }
}
