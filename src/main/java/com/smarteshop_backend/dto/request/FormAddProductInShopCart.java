package com.smarteshop_backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddProductInShopCart {
    private Long id;

    private Double size;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be a positive number")
    private Integer quantity;
}
