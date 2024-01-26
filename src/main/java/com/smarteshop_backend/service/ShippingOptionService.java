package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.ShippingOption;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShippingOptionService {
    ShippingOption save(ShippingOption shippingOption);
    boolean checkName(String name);
    ShippingOption findById(Long id) throws Exception;
    ShippingOption findByName(String name) throws Exception;
    List<ShippingOption> findAll();
}
