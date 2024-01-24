package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.ProductInShopCart;
import com.smarteshop_backend.entity.ShopCart;
import org.springframework.stereotype.Service;

@Service
public interface ShopCartService {
    ShopCart save(ShopCart shopCart);
    ShopCart findById(Long id) throws Exception;
    boolean checkProductAndSize(Long id, Long idProduct, Double size) throws Exception;
    ProductInShopCart addQuantity(Long id, Long idProduct, Double size, Integer quantity) throws Exception;
    boolean removeProduct(Long id, Long idProductInShopCart) throws Exception;
}
