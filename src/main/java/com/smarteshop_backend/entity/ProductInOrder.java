package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @ManyToOne
    @JoinColumn(name = "id_order")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Order order;
}
