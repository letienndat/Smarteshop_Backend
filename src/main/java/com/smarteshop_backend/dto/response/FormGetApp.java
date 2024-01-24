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
public class FormGetApp {
    private Long id;
    private String accountUsername;
    private String accountEmail;
    private List<FormRoleInApp> formRoleInApps;
    private Integer numberProductShopCart;
    private Integer numberProductFavorite;
}
