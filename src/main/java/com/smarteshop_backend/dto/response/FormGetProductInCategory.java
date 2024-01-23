package com.smarteshop_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormGetProductInCategory {
    private Long id;
    private String name;
    private String pathImage;
    private Double price;
    private Double discount;
}
