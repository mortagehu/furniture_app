package com.example.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.furniture.model.Furniture;

public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
    // You can define custom queries here if needed
}