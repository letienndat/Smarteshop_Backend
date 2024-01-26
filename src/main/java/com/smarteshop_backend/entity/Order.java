package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String fullname;

    @NotBlank
    private String province;

    @NotBlank
    private String postalCode;

    @Enumerated
    private Country country;

    @OneToOne
    @JoinColumn(name = "id_shipping_option")
    private ShippingOption shippingOption;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<ProductInOrder> productInOrders;

    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    @OneToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;

    @DecimalMin(value = "0.0", inclusive = true)
    private Double subTotal;

    @DecimalMin(value = "0.0", inclusive = true)
    private Double totalPayment;

    @DecimalMin(value = "0.0", inclusive = true)
    private Double amountReduced;

    private Date dateOrder;
}
