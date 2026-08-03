package com.project.demo.service;

import com.project.demo.entity.Product;

import java.util.List;

/**
 * ProductService interface is called by the <strong>ProductRestController</strong>
 * and communicates with <strong>ProductDAO</strong> for database purposes.</br>
 * Covered by ProductServiceTest.
 */
public interface ProductService {

    /**
     * Returns a list of all the products from the PRODUCT table.
     */
    List<Product> findAll();

    /**
     * Returns a list of all available products from the PRODUCT table.
     */
    List<Product> findAvailable();

    /**
     * Returns a product from the PRODUCT table for given unique code.
     * @param code unique code of the product
     */
    Product findByCode(String code);

    /**
     * Inserts a new product to the PRODUCT table.
     * Product should not have id set.
     * Product's price in USD is converted from EUR using the HNB API.
     * <p>There is no error handling if the HNB API is down or slow.
     *
     * @param product product object to insert into the PRODUCT table
     * @return inserted product object
     */
    Product save(Product product);
}
