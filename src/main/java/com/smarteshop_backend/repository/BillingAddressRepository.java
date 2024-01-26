package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {
    @Modifying
    @Query(value = "DELETE FROM billing_address WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
