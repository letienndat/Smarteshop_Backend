package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.ShippingOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingOptionRepository extends JpaRepository<ShippingOption, Long> {
}
