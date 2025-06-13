package com.example.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.furniture.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
