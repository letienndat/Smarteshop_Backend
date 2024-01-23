package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    Category save(Category category);
    Category findById(Long id) throws Exception;
    boolean checkName(String name);
}
