package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "product_in_order")
public class ProductInOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_product")
    private Product product;

    private Integer quantity;

    @ManyToMany(mappedBy = "productInOrders")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Order> orders;
}
