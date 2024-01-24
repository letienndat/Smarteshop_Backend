package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.ShopCart;
import com.smarteshop_backend.repository.ShopCartRepository;
import com.smarteshop_backend.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopCartServiceImpl implements ShopCartService {
    @Autowired
    private ShopCartRepository shopCartRepository;

    @Override
    public ShopCart save(ShopCart shopCart) {
        return shopCartRepository.save(shopCart);
    }
}
