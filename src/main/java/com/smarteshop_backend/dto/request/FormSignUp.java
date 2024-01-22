package com.smarteshop_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormSignUp {
    private String username;
    private String fullname;
    private String email;
    private String password;
}
