package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    private String pathImage;

    @NotBlank
    private String name;

    private String description;

    @NotEmpty
    private Double precentDiscount;

    @NotEmpty
    private Date expirationDate;

    @NotEmpty
    private Double minPriceApply;

    @NotEmpty
    private Double maxPriceDiscount;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "voucher_category",
            joinColumns = @JoinColumn(name = "id_voucher"),
            inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    private List<Category> categoriesApply;
}
