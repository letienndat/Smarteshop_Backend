package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Brand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {
    List<Brand> findAll();
    Brand save(Brand brand);
    Brand findById(Long id) throws Exception;
    boolean checkName(String name);
    Brand findByName(String name) throws Exception;
}
