package com.project.demo.dao;

import com.project.demo.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAOJpaImpl implements ProductDAO {

    private final EntityManager entityManager;

    @Autowired
    public ProductDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager.createQuery("from Product", Product.class);
        return query.getResultList();
    }

    @Override
    public List<Product> findAvailable() {
        String query = "SELECT p FROM Product p WHERE p.isAvailable = true";
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, Product.class);
        return typedQuery.getResultList();
    }

    @Override
    public Product findById(int id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public Product findByCode(String code) {
        String query = "SELECT p FROM Product p WHERE p.code = :code";
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, Product.class);
        typedQuery.setParameter("code", code);
        return typedQuery.getSingleResultOrNull();
    }

    @Override
    @Transactional
    public Product save(Product product) {
        entityManager.persist(product);
        return findByCode(product.getCode());
    }
}
