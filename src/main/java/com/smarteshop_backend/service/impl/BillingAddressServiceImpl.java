package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.repository.BillingAddressRepository;
import com.smarteshop_backend.service.BillingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillingAddressServiceImpl implements BillingAddressService {
    @Autowired
    private BillingAddressRepository billingAddressRepository;

    @Override
    public void removeBillingAddress(Long id) {
        billingAddressRepository.deleteById(id);
    }
}
