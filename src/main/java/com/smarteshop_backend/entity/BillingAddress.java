package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "billing_address")
public class BillingAddress implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "id_user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
}
