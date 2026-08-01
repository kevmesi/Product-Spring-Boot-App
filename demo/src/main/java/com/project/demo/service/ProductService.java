package com.project.demo.service;

import com.project.demo.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    List<Product> findAvailable();
    Product findByCode(String code);
    Product save(Product product) throws Exception;
}
