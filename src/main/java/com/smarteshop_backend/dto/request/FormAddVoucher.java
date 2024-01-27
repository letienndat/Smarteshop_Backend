package com.smarteshop_backend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddVoucher {
    private String code;

    private String name;

    private MultipartFile image;

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

    private List<Long> idBrand;

    private List<Long> idCategory;
}
