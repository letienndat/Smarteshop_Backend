package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddCategory;
import com.smarteshop_backend.dto.response.FormGetCategory;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.Category;
import com.smarteshop_backend.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/category")
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Add category
     *
     * @param formAddCategory
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addCategory(@Valid @RequestBody FormAddCategory formAddCategory) {
        if (categoryService.checkName(formAddCategory.getName())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "already_exists_category_name", null)
            );
        }

        Category category = new Category(null, formAddCategory.getName(), new ArrayList<>(), new ArrayList<>());
        Category categorySaved = categoryService.save(category);

        FormGetCategory formGetCategory = modelMapper.map(categorySaved, FormGetCategory.class);
        formGetCategory.setFormGetProductInCategories(List.of());

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "add_category_complete", formGetCategory)
        );
    }
}
