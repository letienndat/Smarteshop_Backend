package com.smarteshop_backend.controller;

import com.smarteshop_backend.dto.request.FormAddRole;
import com.smarteshop_backend.dto.response.FormGetRole;
import com.smarteshop_backend.dto.response.MessageResponse;
import com.smarteshop_backend.dto.response.TypeMessage;
import com.smarteshop_backend.entity.Role;
import com.smarteshop_backend.entity.RoleName;
import com.smarteshop_backend.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addRole(@Valid @RequestBody FormAddRole formAddRole) {
        String roleName = formAddRole.getRoleName().trim().toUpperCase();

        Role role = new Role();
        switch (roleName) {
            case "ADMIN":
                role.setRoleName(RoleName.ADMIN);
                break;
            case "USER":
                role.setRoleName(RoleName.USER);
                break;
            default:
                return ResponseEntity.ok(
                        new MessageResponse(TypeMessage.FALD, "add_role_fail_only_user_or_admin")
                );
        }
        boolean checkExistsRole = roleService.checkRoleName(role.getRoleName());

        if (checkExistsRole) {
            return ResponseEntity.ok(
                    new MessageResponse(TypeMessage.FALD, "already_role_name")
            );
        }

        Role roleSaved = roleService.save(role);
        FormGetRole formGetRole = modelMapper.map(roleSaved, FormGetRole.class);

        return ResponseEntity.ok(
                new MessageResponse(TypeMessage.SUCCESS, formGetRole)
        );
    }
}
