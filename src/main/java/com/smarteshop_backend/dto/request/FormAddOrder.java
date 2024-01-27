package com.smarteshop_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddOrder {
    private List<Long> idProductInShopCard;
    private String fullname;
    private String province;
    private String postalCode;
    private String country;
    private Long idShippingOption;
    private String paymentOption;
    private String voucher;
    private boolean saveBillingAddress;
}
