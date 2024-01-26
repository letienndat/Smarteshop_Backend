package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddVoucher;
import com.smarteshop_backend.dto.response.FormGetVoucher;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.Brand;
import com.smarteshop_backend.entity.Category;
import com.smarteshop_backend.entity.User;
import com.smarteshop_backend.entity.Voucher;
import com.smarteshop_backend.service.BrandService;
import com.smarteshop_backend.service.CategoryService;
import com.smarteshop_backend.service.VoucherService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/voucher")
@Validated
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MessageResponse> addVoucher(@Valid @RequestBody FormAddVoucher formAddVoucher) {
        boolean checkExistsCode = voucherService.existsByCode(formAddVoucher.getCode());
        if (checkExistsCode) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "already_exists_code_voucher", null)
            );
        }

        List<Brand> brands = brandService.findByIdIn(formAddVoucher.getIdBrand());
        if (brands.size() != formAddVoucher.getIdBrand().size()) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "id_brand_not_found", null)
            );
        }

        List<Category> categories = categoryService.findByIdIn(formAddVoucher.getIdCategory());
        if (categories.size() != formAddVoucher.getIdCategory().size()) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "id_category_not_found", null)
            );
        }

        List<User> users = new ArrayList<>();

        Voucher voucher = new Voucher(
                null,
                formAddVoucher.getCode(),
                formAddVoucher.getName(),
                formAddVoucher.getPathImage(),
                formAddVoucher.getDescription(),
                formAddVoucher.getPercentDiscount(),
                formAddVoucher.getExpirationDate(),
                formAddVoucher.getMinPriceApply(),
                formAddVoucher.getMaxPriceDiscount(),
                brands,
                categories,
                users
        );

        Voucher voucherSaved = voucherService.save(voucher);
        FormGetVoucher formGetVoucher = modelMapper.map(voucherSaved, FormGetVoucher.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, "add_voucher_complete", formGetVoucher)
        );
    }
}
