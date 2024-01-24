package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.ShopCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopCartRepository extends JpaRepository<ShopCart, Long> {
}
