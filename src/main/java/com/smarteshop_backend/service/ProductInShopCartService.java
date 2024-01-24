package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.ProductInShopCart;
import org.springframework.stereotype.Service;

@Service
public interface ProductInShopCartService {
    ProductInShopCart findById(Long id) throws Exception;
    void removeProductInShopCart(Long id);
}
