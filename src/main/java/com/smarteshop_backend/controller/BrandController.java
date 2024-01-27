package com.smarteshop_backend.controller;

import com.smarteshop_backend.const_app.ConstApp;
import com.smarteshop_backend.dto.request.FormAddBrand;
import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.Brand;
import com.smarteshop_backend.service.BrandService;
import com.smarteshop_backend.util.FileStorageService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/brand")
@Validated
public class BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addBrand(
            @Valid @RequestPart("brand") FormAddBrand formAddBrand,
            @RequestPart(name = "icon", required = false) MultipartFile icon
    ) {
        if (brandService.checkName(formAddBrand.getName())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "already_exists_brand_name", null)
            );
        }

        String pathIconLocal;
        if (icon != null) {
            formAddBrand.setIcon(icon);
            try {
                pathIconLocal = fileStorageService.storeFile(icon, ConstApp.PREFIX_PATH_ICON_BRAND);
            } catch (IOException e) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.FALD, "add_brand_fail_because_can_not_save_icon", null)
                );
            }
        } else {
            pathIconLocal = "/" + ConstApp.PREFIX_PATH_ICON_BRAND + "/" + ConstApp.PREFIX_PATH_DEFAULT;
        }

        Brand brand = new Brand(null, formAddBrand.getName(), pathIconLocal, new ArrayList<>(), new ArrayList<>());
        Brand brandSaved = brandService.save(brand);

        FormGetBrand formGetBrand = modelMapper.map(brandSaved, FormGetBrand.class);
        formGetBrand.setFormGetProductInBrands(List.of());

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "add_brand_complete", formGetBrand)
        );
    }

    @GetMapping
    public ResponseEntity<MessageResponse> load(@Valid @RequestParam(name = "name", required = false) String name) {
        if (name == null) {
            List<Brand> brands = brandService.findAll();
            List<FormGetBrand> formGetBrands = brands.stream()
                    .map(brand -> {
                        FormGetBrand formGetBrand = modelMapper.map(brand, FormGetBrand.class);
                        List<FormGetProductInBrand> formGetProductInBrands = brand.getProducts().stream()
                                .map(product -> modelMapper.map(product, FormGetProductInBrand.class))
                                .toList();
                        formGetBrand.setFormGetProductInBrands(formGetProductInBrands);

                        return formGetBrand;
                    }).toList();

            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "response_success", formGetBrands)
            );
        }

        try {
            Brand brand = brandService.findByName(name.trim());

            FormGetBrand formGetBrand = modelMapper.map(brand, FormGetBrand.class);
            List<FormGetProductInBrand> formGetProductInBrands = brand.getProducts().stream()
                    .map(product -> modelMapper.map(product, FormGetProductInBrand.class))
                    .toList();
            formGetBrand.setFormGetProductInBrands(formGetProductInBrands);

            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "response_success", formGetBrand)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_brand_name", null)
            );
        }
    }
}
