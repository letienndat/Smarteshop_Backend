package com.smarteshop_backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentOption {
    ONLINE_BANKING("Online Banking"),
    CREDIT_DEBIT_CARD("Credit/Debit Card"),
    PAYPAL("Paypal");

    @Getter
    private final String displayName;
}
