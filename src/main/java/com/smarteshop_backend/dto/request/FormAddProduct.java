package com.smarteshop_backend.dto.request;

import com.smarteshop_backend.entity.ImageDemo;
import com.smarteshop_backend.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddProduct {
    private String name;
    private String pathImage;
    private String description;
    private Long idBrand;
    private Long idCategory;
    private Double price;
    private Double discount;
    private List<String> pathImageDemos;
    private List<Double> sizes;
}
