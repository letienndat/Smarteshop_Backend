package com.smarteshop_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormGetUser {
    private Long id;
    private String fullname;
    private String accountUsername;
    private String accountEmail;
    private Boolean accountEnabled;
}
