package com.example.furniture.controller;

import com.example.furniture.model.Category;
import com.example.furniture.model.Furniture;
import com.example.furniture.repository.CategoryRepository;
import com.example.furniture.repository.FurnitureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FurnitureControllerTest {

    @Mock
    private FurnitureRepository furnitureRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Model model;

    @InjectMocks
    private FurnitureController furnitureController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListFurniture() {
        // Arrange
        List<Furniture> furnitureList = Arrays.asList(new Furniture(), new Furniture());
        when(furnitureRepository.findAll()).thenReturn(furnitureList);

        // Act
        String viewName = furnitureController.listFurniture(model);

        // Assert
        assertEquals("index", viewName);
        verify(model).addAttribute("furnitureList", furnitureList);
    }

    @Test
    void testShowCreateForm() {
        // Arrange
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        String viewName = furnitureController.showCreateForm(model);

        // Assert
        assertEquals("create", viewName);
        verify(model).addAttribute(eq("furniture"), any(Furniture.class));
        verify(model).addAttribute("categories", categories);
    }

    @Test
    void testSaveFurnitureWithNewCategory() {
        // Arrange
        Furniture furniture = new Furniture();
        String newCategoryName = "New Category";
        
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String viewName = furnitureController.saveFurniture(furniture, newCategoryName);

        // Assert
        assertEquals("redirect:/furniture", viewName);
        verify(categoryRepository).save(categoryCaptor.capture());
        verify(furnitureRepository).save(furniture);
        
        Category savedCategory = categoryCaptor.getValue();
        assertEquals(newCategoryName, savedCategory.getName());
        assertEquals(savedCategory, furniture.getCategory());
    }

    @Test
    void testSaveFurnitureWithExistingCategory() {
        // Arrange
        Furniture furniture = new Furniture();
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        furniture.setCategory(existingCategory);
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));

        // Act
        String viewName = furnitureController.saveFurniture(furniture, null);

        // Assert
        assertEquals("redirect:/furniture", viewName);
        verify(furnitureRepository).save(furniture);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testShowEditForm() {
        // Arrange
        Long furnitureId = 1L;
        Furniture furniture = new Furniture();
        List<Category> categories = Arrays.asList(new Category(), new Category());
        
        when(furnitureRepository.findById(furnitureId)).thenReturn(Optional.of(furniture));
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        String viewName = furnitureController.showEditForm(furnitureId, model);

        // Assert
        assertEquals("edit", viewName);
        verify(model).addAttribute("furniture", furniture);
        verify(model).addAttribute("categories", categories);
    }

    @Test
    void testDeleteFurniture() {
        // Arrange
        Long furnitureId = 1L;
        Furniture furniture = new Furniture();
        furniture.setId(furnitureId);
        when(furnitureRepository.findById(furnitureId)).thenReturn(Optional.of(furniture));

        // Act
        String viewName = furnitureController.deleteFurniture(furnitureId);

        // Assert
        assertEquals("redirect:/furniture", viewName);
        verify(furnitureRepository).delete(furniture);  // Changed from deleteById to delete
    }

    @Test
    void testUpdateFurniture() {
        // Arrange
        Long furnitureId = 1L;
        Furniture furniture = new Furniture();
        Category category = new Category();
        category.setId(1L);
        furniture.setCategory(category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        String viewName = furnitureController.updateFurniture(furnitureId, furniture);

        // Assert
        assertEquals("redirect:/furniture", viewName);
        verify(furnitureRepository).save(furniture);
    }
}