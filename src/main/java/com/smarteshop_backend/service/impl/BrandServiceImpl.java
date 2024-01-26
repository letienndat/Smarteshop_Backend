package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.Brand;
import com.smarteshop_backend.entity.Category;
import com.smarteshop_backend.repository.BrandRepository;
import com.smarteshop_backend.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand findById(Long id) throws Exception {
        return brandRepository.findById(id).orElseThrow(() -> new Exception("Cannot find brand with id = " + id));
    }

    @Override
    public boolean checkName(String name) {
        return brandRepository.existsByName(name);
    }

    @Override
    public Brand findByName(String name) throws Exception {
        return brandRepository.findByName(name).orElseThrow(() -> new Exception("Cannot brand with name = " + name));
    }

    @Override
    public List<Brand> findByIdIn(List<Long> ids) {
        return brandRepository.findByIdIn(ids);
    }
}
