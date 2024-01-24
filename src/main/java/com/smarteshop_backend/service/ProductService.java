package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product save(Product product);
    Product findById(Long id) throws Exception;
    List<Product> findAll();
    boolean checkSizeInProduct(Long id, Double size) throws Exception;
}
