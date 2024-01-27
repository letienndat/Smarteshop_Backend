package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.ProductInShopCart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductInShopCartService {
    ProductInShopCart findById(Long id) throws Exception;
    void removeProductInShopCart(Long id);
    List<ProductInShopCart> findByIdIn(List<Long> ids);
    void removeByIds(List<Long> ids);
}
