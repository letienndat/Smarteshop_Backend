package com.smarteshop_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "accounts")
public class Account implements Serializable {
    @Id
    private String username;

    @Email
    private String email;

    @NotNull
    private String password;

    @NotBlank
    private String verificationCode;

    @NotEmpty
    private Boolean enabled;

    @NotEmpty
    private Date expiredTime;
}
