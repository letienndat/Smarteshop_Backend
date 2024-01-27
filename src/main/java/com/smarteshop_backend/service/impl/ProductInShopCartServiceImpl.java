package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.ProductInShopCart;
import com.smarteshop_backend.repository.ProductInShopCartRepository;
import com.smarteshop_backend.service.ProductInShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInShopCartServiceImpl implements ProductInShopCartService {
    @Autowired
    private ProductInShopCartRepository productInShopCartRepository;

    @Override
    public ProductInShopCart findById(Long id) throws Exception {
        return productInShopCartRepository.findById(id).orElseThrow(() -> new Exception("Cannot find product in shop cart with id = " + id));
    }

    @Override
    public void removeProductInShopCart(Long id) {
        productInShopCartRepository.deleteById(id);
    }

    @Override
    public List<ProductInShopCart> findByIdIn(List<Long> ids) {
        return productInShopCartRepository.findByIdIn(ids);
    }

    @Override
    public void removeByIds(List<Long> ids) {
        productInShopCartRepository.deleteByIdIn(ids);
    }
}
