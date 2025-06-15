package com.example.furniture.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FurnitureTest {

    @Test
    void testFurnitureGettersAndSetters() {
        // Arrange
        Furniture furniture = new Furniture();
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        // Act
        furniture.setId(1L);
        furniture.setName("Test Chair");
        furniture.setPrice(99.99);
        furniture.setDescription("A test chair");
        furniture.setImageUrl("http://test.com/image.jpg");
        furniture.setCategory(category);

        // Assert
        assertEquals(1L, furniture.getId());
        assertEquals("Test Chair", furniture.getName());
        assertEquals(99.99, furniture.getPrice());
        assertEquals("A test chair", furniture.getDescription());
        assertEquals("http://test.com/image.jpg", furniture.getImageUrl());
        assertEquals(category, furniture.getCategory());
    }

    @Test
    void testFurnitureConstructor() {
        // Arrange & Act
        Furniture furniture = new Furniture();

        // Assert
        assertNull(furniture.getId());
        assertNull(furniture.getName());
        assertEquals(0.0, furniture.getPrice());
        assertNull(furniture.getDescription());
        assertNull(furniture.getImageUrl());
        assertNull(furniture.getCategory());
    }
}