package com.smarteshop_backend.controller;

import com.smarteshop_backend.const_app.ConstApp;
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
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Add voucher
     *
     * @param formAddVoucher
     * @param image
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MessageResponse> addVoucher(
            @Valid @RequestPart("voucher") FormAddVoucher formAddVoucher,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
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

        String pathImageLocal;
        if (image != null) {
            formAddVoucher.setImage(image);
            try {
                pathImageLocal = fileStorageService.storeFile(image, ConstApp.PREFIX_IMAGE_VOUCHER);
            } catch (IOException e) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.FALD, "add_voucher_fail_because_can_not_save_image", null)
                );
            }
        } else {
            pathImageLocal = "/" + ConstApp.PREFIX_IMAGE_VOUCHER + "/" + ConstApp.PREFIX_PATH_DEFAULT;
        }

        Voucher voucher = new Voucher(
                null,
                formAddVoucher.getCode(),
                formAddVoucher.getName(),
                pathImageLocal,
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
