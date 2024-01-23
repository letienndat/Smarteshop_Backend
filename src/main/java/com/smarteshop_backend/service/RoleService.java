package com.smarteshop_backend.service;

import com.smarteshop_backend.entity.Role;
import com.smarteshop_backend.entity.RoleName;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    boolean checkRoleName(RoleName roleName);
    Role save(Role role);
    Role findByRoleName(RoleName roleName) throws Exception;
}
