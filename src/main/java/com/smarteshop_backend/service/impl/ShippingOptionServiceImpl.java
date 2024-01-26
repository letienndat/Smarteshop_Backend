package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.ShippingOption;
import com.smarteshop_backend.repository.ShippingOptionRepository;
import com.smarteshop_backend.service.ShippingOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingOptionServiceImpl implements ShippingOptionService {
    @Autowired
    private ShippingOptionRepository shippingOptionRepository;


    @Override
    public ShippingOption save(ShippingOption shippingOption) {
        return shippingOptionRepository.save(shippingOption);
    }

    @Override
    public boolean checkName(String name) {
        return shippingOptionRepository.existsByName(name);
    }

    @Override
    public ShippingOption findById(Long id) throws Exception {
        return shippingOptionRepository.findById(id).orElseThrow(() -> new Exception("Cannot find shipping option with id = " + id));
    }

    @Override
    public ShippingOption findByName(String name) throws Exception {
        return shippingOptionRepository.findByName(name).orElseThrow(() -> new Exception("Cannot find shipping option with name = " + name));
    }

    @Override
    public List<ShippingOption> findAll() {
        return shippingOptionRepository.findAll();
    }
}
