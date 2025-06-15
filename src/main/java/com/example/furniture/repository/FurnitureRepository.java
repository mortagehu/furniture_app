package com.example.furniture.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.furniture.model.Furniture;
import java.util.List;

public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
    List<Furniture> findByNameContainingIgnoreCase(String query);
}