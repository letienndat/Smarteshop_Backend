package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "product_in_shop_cart")
public class ProductInShopCart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_product")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;

    @OneToOne
    @JoinColumn(name = "id_size")
    private Size size;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be a positive number")
    private Integer quantity;

    private Date updateAt;

    @ManyToOne
    @JoinColumn(name = "id_shop_cart")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ShopCart shopCart;
}
