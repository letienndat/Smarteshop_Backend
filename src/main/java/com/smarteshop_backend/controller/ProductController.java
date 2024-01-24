package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddProduct;
import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.*;
import com.smarteshop_backend.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ImageDemoService imageDemoService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addProduct(@Valid @RequestBody FormAddProduct formAddProduct) throws Exception {
        Category category = categoryService.findById(formAddProduct.getIdCategory());
        if (category == null) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_category_name")
            );
        }

        Brand brand = brandService.findById(formAddProduct.getIdBrand());
        if (brand == null) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "not_exists_brand_name")
            );
        }

        Product product = new Product(
                null,
                formAddProduct.getName(),
                formAddProduct.getPathImage(),
                formAddProduct.getDescription(),
                brand,
                category,
                formAddProduct.getPrice(),
                formAddProduct.getDiscount(),
                null,
                null,
                new ArrayList<>()
        );

        List<ImageDemo> imageDemos = new ArrayList<>();
        formAddProduct.getPathImageDemos().forEach(path -> {
            ImageDemo imageDemoSaved = new ImageDemo(null, path, product);
            imageDemos.add(imageDemoSaved);
        });

        List<Size> sizes = new ArrayList<>();
        formAddProduct.getSizes().forEach(size -> {
            Size sizeSaved = null;
            if (!sizeService.checkSize(size)) {
                sizeSaved = new Size(null, size, null);
            } else {
                sizeSaved = sizeService.findBySize(size);
            }
            sizes.add(sizeSaved);
        });

        product.setImageDemos(imageDemos);
        product.setSizes(sizes);

        Product productSaved = productService.save(product);

        FormGetProduct formGetProduct = modelMapper.map(productSaved, FormGetProduct.class);

        FormGetBrandInProduct formGetBrandInProduct = modelMapper.map(productSaved.getBrand(), FormGetBrandInProduct.class);
        FormGetCategoryInProduct formGetCategoryInProduct = modelMapper.map(productSaved.getCategory(), FormGetCategoryInProduct.class);
        List<FormImageDemoInProduct> formImageDemoInProducts = productSaved.getImageDemos().stream()
                .map(imageDemo -> modelMapper.map(imageDemo, FormImageDemoInProduct.class))
                .toList();
        List<FormSizeInProduct> formSizeInProducts = productSaved.getSizes().stream()
                .map(size -> modelMapper.map(size, FormSizeInProduct.class))
                .toList();
        List<FormUserFavoriteInProduct> formUserFavoriteInProducts = productSaved.getUserFavorites().stream()
                .map(user -> modelMapper.map(user, FormUserFavoriteInProduct.class))
                .toList();

        formGetProduct.setFormGetBrandInProduct(formGetBrandInProduct);
        formGetProduct.setFormGetCategoryInProduct(formGetCategoryInProduct);
        formGetProduct.setFormImageDemoInProducts(formImageDemoInProducts);
        formGetProduct.setFormSizeInProducts(formSizeInProducts);
        formGetProduct.setFormUserFavoriteInProducts(formUserFavoriteInProducts);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, formGetProduct)
        );
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<MessageResponse> showDetails(@Valid @PathVariable(name = "id") Long id) {
        try {
            Product product = productService.findById(id);
            FormGetProduct formGetProduct = modelMapper.map(product, FormGetProduct.class);

            FormGetBrandInProduct formGetBrandInProduct = modelMapper.map(product.getBrand(), FormGetBrandInProduct.class);
            FormGetCategoryInProduct formGetCategoryInProduct = modelMapper.map(product.getCategory(), FormGetCategoryInProduct.class);
            List<FormImageDemoInProduct> formImageDemoInProducts = product.getImageDemos().stream()
                    .map(imageDemo -> modelMapper.map(imageDemo, FormImageDemoInProduct.class))
                    .toList();
            List<FormSizeInProduct> formSizeInProducts = product.getSizes().stream()
                    .map(size -> modelMapper.map(size, FormSizeInProduct.class))
                    .toList();
            List<FormUserFavoriteInProduct> formUserFavoriteInProducts = product.getUserFavorites().stream()
                    .map(user -> modelMapper.map(user, FormUserFavoriteInProduct.class))
                    .toList();

            formGetProduct.setFormGetBrandInProduct(formGetBrandInProduct);
            formGetProduct.setFormGetCategoryInProduct(formGetCategoryInProduct);
            formGetProduct.setFormImageDemoInProducts(formImageDemoInProducts);
            formGetProduct.setFormSizeInProducts(formSizeInProducts);
            formGetProduct.setFormUserFavoriteInProducts(formUserFavoriteInProducts);

            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, formGetProduct)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.SUCCESS, "not_exists_product_id")
            );
        }
    }
}
