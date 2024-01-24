package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "product_in_shop_cart")
public class ProductInShopCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @OneToOne
    @JoinColumn(name = "id_size")
    private Size size;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "id_shop_cart")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ShopCart shopCart;
}
