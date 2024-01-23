package com.smarteshop_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormHomeLoad {
    private Long id;
    private String fullname;
    private String accountUsername;
    private String accountEmail;
    private String pathAvatar;
    private List<FormBrandInHome> formBrandInHomes;
    private List<FormProductHome> formProductHomes;
}
