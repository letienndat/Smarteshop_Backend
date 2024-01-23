package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddBrand;
import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.Brand;
import com.smarteshop_backend.entity.Category;
import com.smarteshop_backend.service.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addBrand(@Valid @RequestBody FormAddBrand formAddBrand) {
        if (brandService.checkName(formAddBrand.getName())) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "already_exists_brand_name")
            );
        }

        Brand brand = new Brand(null, formAddBrand.getName(), formAddBrand.getPathIcon(), List.of(), List.of());
        Brand brandSaved = brandService.save(brand);

        FormGetBrand formGetBrand = modelMapper.map(brandSaved, FormGetBrand.class);
        formGetBrand.setFormGetProductInBrands(List.of());

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, formGetBrand)
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
                    new MessageResponse(TypeMessage.SUCCESS, formGetBrands)
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
                    new MessageResponse(TypeMessage.SUCCESS, formGetBrand)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_brand_name")
            );
        }
    }
}
