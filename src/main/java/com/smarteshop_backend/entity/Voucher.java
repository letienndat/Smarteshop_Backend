package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "vouchers")
public class Voucher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String code;

    @NotBlank
    private String name;

    @NotBlank
    private String pathImage;

    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    private Double percentDiscount;

    private Date expirationDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private Double minPriceApply;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private Double maxPriceDiscount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "voucher_brand",
            joinColumns = @JoinColumn(name = "id_voucher"),
            inverseJoinColumns = @JoinColumn(name = "id_brand")
    )
    private List<Brand> brandsApply;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "voucher_category",
            joinColumns = @JoinColumn(name = "id_voucher"),
            inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    private List<Category> categoriesApply;

    @ManyToMany(mappedBy = "vouchers")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<User> users;
}
