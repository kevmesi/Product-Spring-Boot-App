package com.project.demo.dao;

import com.project.demo.entity.Product;

import java.util.List;

public interface ProductDAO {

    /**
     * Returns a list of all the products from the PRODUCT table.
     */
    List<Product> findAll();

    /**
     * Returns a list of all available products from the PRODUCT table.
     */
    List<Product> findAvailable();

    /**
     * Returns a product from the PRODUCT table for given id.
     * @param id product id
     */
    Product findById(int id);

    /**
     * Returns a product from the PRODUCT table for given unique code.
     * @param code unique code of the product
     */
    Product findByCode(String code);

    /**
     * Inserts a new product to the PRODUCT table.
     * Product should not have id set.
     * @param product product object to insert into the PRODUCT table
     * @return inserted product object
     */
    Product save(Product product);

}
