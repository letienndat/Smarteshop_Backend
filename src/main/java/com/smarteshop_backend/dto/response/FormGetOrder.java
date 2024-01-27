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
public class FormGetOrder {
    private Long id;
    private Long userId;
    private String fullname;
    private String province;
    private String postalCode;
    private String country;
    private String shippingOptionName;
    private Double shippingOptionPrice;
    private List<FormGetProductInOrder> formGetProductInOrders;
    private String paymentOption;
    private String voucherCode;
    private Double subTotal;
    private Double amountReduced;
    private Double totalPayment;
    private Date dateOrder;
}
