package com.smarteshop_backend.controller;

import com.smarteshop_backend.const_app.ConstApp;
import com.smarteshop_backend.dto.request.FormAddProduct;
import com.smarteshop_backend.dto.response.*;
import com.smarteshop_backend.entity.*;
import com.smarteshop_backend.service.*;
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
@RequestMapping("/api/product")
@Validated
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
    private FileStorageService fileStorageService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Add product
     *
     * @param formAddProduct
     * @param image
     * @param imageDemos
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addProduct(
            @Valid @RequestPart("product") FormAddProduct formAddProduct,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "image-demos", required = false) List<MultipartFile> imageDemos
    ) throws Exception {

        Category category = categoryService.findById(formAddProduct.getIdCategory());
        if (category == null) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_category_name", null)
            );
        }

        Brand brand = brandService.findById(formAddProduct.getIdBrand());
        if (brand == null) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_brand_name", null)
            );
        }

        String pathImageLocal;
        if (image != null) {
            formAddProduct.setImage(image);
            try {
                pathImageLocal = fileStorageService.storeFile(image, ConstApp.PREFIX_PATH_IMAGE_PRODUCT);
            } catch (IOException e) {
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.FALD, "add_product_fail_because_can_not_save_image", null)
                );
            }
        } else {
            pathImageLocal = "/" + ConstApp.PREFIX_PATH_IMAGE_PRODUCT + "/" + ConstApp.PREFIX_PATH_DEFAULT;
        }

        List<String> pathImageDemosLocal = new ArrayList<>();
        if (imageDemos != null) {
            formAddProduct.setImageDemos(imageDemos);
            for (MultipartFile imageDemo : imageDemos) {
                try {
                    String pathImageDemo = fileStorageService.storeFile(imageDemo, ConstApp.PREFIX_PATH_IMAGE_PRODUCT_DEMO);
                    pathImageDemosLocal.add(pathImageDemo);
                } catch (IOException e) {
                    return ResponseEntity.ok(
                            new MessageResponse(TypeMessage.FALD, "add_product_fail_because_can_not_save_image_demo", null)
                    );
                }
            }
        }

        Product product = new Product(
                null,
                formAddProduct.getName(),
                pathImageLocal,
                formAddProduct.getDescription(),
                brand,
                category,
                formAddProduct.getPrice(),
                formAddProduct.getDiscount(),
                null,
                null,
                new ArrayList<>(),
                null,
                null
        );

        List<ImageDemo> imageDemos1 = new ArrayList<>();
        pathImageDemosLocal.forEach(path -> {
            ImageDemo imageDemoSaved = new ImageDemo(null, path, product);
            imageDemos1.add(imageDemoSaved);
        });

        List<Size> sizes = new ArrayList<>();
        for (Double size : formAddProduct.getSizes()) {
            Size sizeSaved;
            if (!sizeService.checkSize(size)) {
                sizeSaved = new Size(null, size, null);
            } else {
                try {
                    sizeSaved = sizeService.findBySize(size);
                } catch (Exception e) {
                    return ResponseEntity.ok(
                            new MessageResponse(TypeMessage.FALD, "not_exists_size", null)
                    );
                }
            }
            sizes.add(sizeSaved);
        }

        product.setImageDemos(imageDemos1);
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
                new MessageResponse(TypeMessage.SUCCESS, "add_product_complete", formGetProduct)
        );
    }

    /**
     * Get detail product
     *
     * @param id
     * @return
     */
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
                    new MessageResponse(TypeMessage.SUCCESS, "response_success", formGetProduct)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "not_exists_product_id", null)
            );
        }
    }
}
