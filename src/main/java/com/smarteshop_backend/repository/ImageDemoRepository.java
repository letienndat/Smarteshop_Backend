package com.smarteshop_backend.repository;

import com.smarteshop_backend.entity.ImageDemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDemoRepository extends JpaRepository<ImageDemo, Long> {
}
