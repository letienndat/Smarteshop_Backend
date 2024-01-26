package com.smarteshop_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormUpdateBillingAddress {
    private Long id;
    private String fullname;
    private String province;
    private String postalCode;
    private String country;
}
