package com.project.demo.service;

import com.project.demo.dao.ProductDAO;
import com.project.demo.entity.Product;
import com.project.demo.share.CurrencyConverter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> findAvailable() {
        return productDAO.findAvailable();
    }

    @Override
    public Product findByCode(String code) {
        return productDAO.findByCode(code);
    }

    @Override
    public Product save(Product product) {

        BigDecimal priceEUR = product.getPriceEUR();
        BigDecimal priceUSD = CurrencyConverter.convertEURtoUSD(priceEUR);
        product.setPriceUSD(priceUSD);

        return productDAO.save(product);
    }
}
