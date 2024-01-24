package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @OneToOne
    @JoinColumn(name = "id_billing_address")
    private BillingAddress billingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<ProductInOrder> productInOrders;

    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    @OneToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;

    private Double subTotal;

    private Double totalPayment;

    private Double amountReduced;

    private Date dateOrder;
}
