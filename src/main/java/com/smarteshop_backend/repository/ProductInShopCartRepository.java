package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.ProductInShopCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInShopCartRepository extends JpaRepository<ProductInShopCart, Long> {
    @Modifying
    @Query(value = "DELETE FROM product_in_shop_cart WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
