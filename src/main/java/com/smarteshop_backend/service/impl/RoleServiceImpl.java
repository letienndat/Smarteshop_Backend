package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.Role;
import com.smarteshop_backend.entity.RoleName;
import com.smarteshop_backend.repository.RoleRepository;
import com.smarteshop_backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean checkRoleName(RoleName roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findByRoleName(RoleName roleName) throws Exception {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new Exception("Cannot role with roleName = " + roleName));
    }
}
