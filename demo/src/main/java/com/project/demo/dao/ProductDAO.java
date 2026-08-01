package com.project.demo.dao;

import com.project.demo.entity.Product;

import java.util.List;

public interface ProductDAO {

    List<Product> findAll();
    List<Product> findAvailable();
    Product findById(int id);
    Product findByCode(String code);
    Product save(Product product);

}
