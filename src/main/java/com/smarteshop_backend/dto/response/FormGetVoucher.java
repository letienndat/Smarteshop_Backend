package com.smarteshop_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormGetVoucher {
    private Long id;
    private String code;
    private String name;
    private String pathImage;
    private String description;
    private Double percentDiscount;
    private Date expirationDate;
    private Double minPriceApply;
    private Double maxPriceDiscount;
    private List<FormGetBrandInVoucher> brands;
    private List<FormGetCategoryInVoucher> categories;
}
