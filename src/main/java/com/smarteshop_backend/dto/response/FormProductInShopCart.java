package com.smarteshop_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormProductInShopCart {
    private Long id;
    private Long productId;
    private String productName;
    private String productPathImage;
    private String productBrandName;
    private String productCategoryName;
    private Double productPrice;
    private Double productDiscount;
    private Double sizeNumber;
    private Integer quantity;
}
