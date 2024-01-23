package com.smarteshop_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormGetProduct {
    private Long id;
    private String name;
    private String pathImage;
    private String description;
    private FormGetBrandInProduct formGetBrandInProduct;
    private FormGetCategoryInProduct formGetCategoryInProduct;
    private Double price;
    private Double discount;
    private List<FormImageDemoInProduct> formImageDemoInProducts;
    private List<FormSizeInProduct> formSizeInProducts;
    private List<FormUserFavoriteInProduct> formUserFavoriteInProducts;
}
