package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.ProductInShopCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInShopCartRepository extends JpaRepository<ProductInShopCart, Long> {
}
