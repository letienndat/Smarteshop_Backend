package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "product_in_order")
public class ProductInOrder implements Serializable {
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
    @JoinColumn(name = "id_order")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Order order;
}
