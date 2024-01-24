package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.Product;
import com.smarteshop_backend.entity.Size;
import com.smarteshop_backend.repository.ProductRepository;
import com.smarteshop_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) throws Exception {
        return productRepository.findById(id).orElseThrow(() -> new Exception("Cannot find product with id = " + id));
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public boolean checkSizeInProduct(Long id, Double size) throws Exception {
        Product product = findById(id);
        for (Size productSize : product.getSizes()) {
            if (productSize.getNumber().equals(size)) return true;
        }
        return false;
    }
}
