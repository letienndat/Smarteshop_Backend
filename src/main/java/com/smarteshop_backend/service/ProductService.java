package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    Product save(Product product);
    Product findById(Long id) throws Exception;
}
