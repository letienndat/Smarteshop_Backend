package com.smarteshop_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "sizes")
public class Size implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private Double size;

    @ManyToMany(mappedBy = "sizes")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Product> products;
}
