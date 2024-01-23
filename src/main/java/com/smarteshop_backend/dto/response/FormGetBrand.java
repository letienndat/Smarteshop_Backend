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
public class FormGetBrand {
    private Long id;
    private String name;
    private String pathIcon;
    private List<FormGetProductInBrand> formGetProductInBrands;
}
