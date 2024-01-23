package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.Category;
import com.smarteshop_backend.repository.CategoryRepository;
import com.smarteshop_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(Long id) throws Exception {
        return categoryRepository.findById(id).orElseThrow(() -> new Exception("Cannot find category with id = " + id));
    }

    @Override
    public boolean checkName(String name) {
        return categoryRepository.existsByName(name);
    }
}
