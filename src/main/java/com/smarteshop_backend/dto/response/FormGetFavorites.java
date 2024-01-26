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
public class FormGetFavorites {
    private Long id;
    private String accountUsername;
    private List<FormGetProductInFavorites> formGetProductInFavorites;
}
