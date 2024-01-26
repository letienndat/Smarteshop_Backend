package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    boolean existsByCode(String code);
    Optional<Voucher> findByCode(String code);
}
