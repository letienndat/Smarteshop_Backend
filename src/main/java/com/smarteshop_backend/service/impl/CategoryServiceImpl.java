package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.Category;
import com.smarteshop_backend.repository.CategoryRepository;
import com.smarteshop_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Save category
     *
     * @param category
     * @return
     */
    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Find category by id
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Category findById(Long id) throws Exception {
        return categoryRepository.findById(id).orElseThrow(() -> new Exception("Cannot find category with id = " + id));
    }

    /**
     * Check exists category name
     *
     * @param name
     * @return
     */
    @Override
    public boolean checkName(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * Find categories by list id
     *
     * @param ids
     * @return
     */
    @Override
    public List<Category> findByIdIn(List<Long> ids) {
        return categoryRepository.findByIdIn(ids);
    }
}
