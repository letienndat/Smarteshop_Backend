package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.ShopCart;
import org.springframework.stereotype.Service;

@Service
public interface ShopCartService {
    ShopCart save(ShopCart shopCart);
}
