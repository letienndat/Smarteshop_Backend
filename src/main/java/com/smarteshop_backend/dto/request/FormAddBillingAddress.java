package com.smarteshop_backend.dto.request;

import com.smarteshop_backend.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddBillingAddress {
    private String fullname;
    private String province;
    private String postalCode;
    private String country;
}
