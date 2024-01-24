package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.Brand;
import com.smarteshop_backend.entity.User;
import com.smarteshop_backend.service.BrandService;
import com.smarteshop_backend.service.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BrandService brandService;

    @GetMapping
    public ResponseEntity<MessageResponse> load() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Brand> brands = brandService.findAll();
        User user = userService.getByUsername(username);

        List<FormBrandInHome> formBrandInHomes = brands.stream()
                .map(brand -> modelMapper.map(brand, FormBrandInHome.class))
                .toList();

        List<FormProductHome> formProductHomes = new ArrayList<>();
        if (!brands.isEmpty()) {
            formProductHomes = brands.get(0).getProducts().stream()
                    .map(product -> modelMapper.map(product, FormProductHome.class))
                    .toList();
        }

        FormGetHome formGetHome = modelMapper.map(user, FormGetHome.class);
        formGetHome.setFormBrandInHomes(formBrandInHomes);
        formGetHome.setFormProductHomes(formProductHomes);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, formGetHome)
        );
    }
}
