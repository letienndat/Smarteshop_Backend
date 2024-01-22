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
@Table(name = "image_demos")
public class ImageDemo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String pathImage;

    @ManyToOne
    @JoinColumn(name = "id_product")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;
}
