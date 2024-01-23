package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String pathImage;

    private String description;

    @ManyToOne
    @JoinColumn(name = "id_brand")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "id_category")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Category category;

    @NotEmpty
    private Double price;

    @NotEmpty
    private Double discount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ImageDemo> imageDemos;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "product_size",
            joinColumns = @JoinColumn(name = "id_product"),
            inverseJoinColumns = @JoinColumn(name = "id_size")
    )
    private List<Size> sizes;

    @ManyToMany(mappedBy = "productFavorites")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<User> userFavorites;
}
