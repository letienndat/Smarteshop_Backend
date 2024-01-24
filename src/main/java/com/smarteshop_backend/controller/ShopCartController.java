package com.smarteshop_backend.controller;

import com.smarteshop_backend.service.ShopCartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop-cart")
public class ShopCartController {
    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private ModelMapper modelMapper;
}
