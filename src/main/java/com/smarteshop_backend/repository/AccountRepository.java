package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByEmail(String email);
    Account findByUsername(String username);
    boolean existsByUsername(String username);
}
