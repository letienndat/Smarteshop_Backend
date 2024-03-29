package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    boolean existsByNumber(Double size);
    Optional<Size> findByNumber(Double size);
}
