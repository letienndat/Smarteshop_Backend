package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Category save(Category category);
    Category findById(Long id) throws Exception;
    boolean checkName(String name);
    List<Category> findByIdIn(List<Long> ids);
}
