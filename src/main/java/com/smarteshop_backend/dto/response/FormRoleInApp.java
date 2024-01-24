package com.smarteshop_backend.dto.response;

import com.smarteshop_backend.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormRoleInApp {
    private RoleName roleName;
}
