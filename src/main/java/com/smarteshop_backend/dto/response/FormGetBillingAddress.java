package com.smarteshop_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormGetBillingAddress {
    private Long id;
    private Long userId;
    private String fullname;
    private String province;
    private String postalCode;
    private String country;
}
