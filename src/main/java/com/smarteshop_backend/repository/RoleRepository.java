package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.Role;
import com.smarteshop_backend.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByRoleName(RoleName roleName);
    Optional<Role> findByRoleName(RoleName roleName);
}
