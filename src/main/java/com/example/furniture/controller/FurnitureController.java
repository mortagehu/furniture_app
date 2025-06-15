package com.example.furniture.controller;

import com.example.furniture.model.Furniture;
import com.example.furniture.model.Category;
import com.example.furniture.repository.FurnitureRepository;
import com.example.furniture.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/furniture")
public class FurnitureController {

    private  FurnitureRepository furnitureRepository;
    private  CategoryRepository categoryRepository;

    @Autowired
    public FurnitureController(FurnitureRepository furnitureRepository, CategoryRepository categoryRepository) {
        this.furnitureRepository = furnitureRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String listFurniture(Model model) {
        List<Furniture> furnitureList = furnitureRepository.findAll();
        model.addAttribute("furnitureList", furnitureList);
        return "index"; // Your main view
    }
@GetMapping("/create")
public String showCreateForm(Model model) {
    model.addAttribute("furniture", new Furniture());
    model.addAttribute("categories", categoryRepository.findAll());
    return "create";
}

@PostMapping("/save")
public String saveFurniture(@ModelAttribute Furniture furniture,
                            @RequestParam(required = false) String newCategoryName) {

    if (newCategoryName != null && !newCategoryName.trim().isEmpty()) {
        Category newCategory = new Category();
        newCategory.setName(newCategoryName.trim());
        categoryRepository.save(newCategory);
        furniture.setCategory(newCategory);
    } else if (furniture.getCategory() != null && furniture.getCategory().getId() != null) {
        Category existingCategory = categoryRepository.findById(furniture.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        furniture.setCategory(existingCategory);
    } else {
        throw new IllegalArgumentException("Category is required.");
    }

    furnitureRepository.save(furniture);
    return "redirect:/furniture";
}

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Furniture furniture = furnitureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid furniture ID: " + id));
        model.addAttribute("furniture", furniture);
        model.addAttribute("categories", categoryRepository.findAll());
        return "edit";
    }

    @PostMapping("/update/{id}")
    public String updateFurniture(@PathVariable Long id, @ModelAttribute Furniture furniture) {
        furniture.setId(id);
        furnitureRepository.save(furniture);
        return "redirect:/furniture";
    }

    @GetMapping("/delete/{id}")
    public String deleteFurniture(@PathVariable Long id) {
        Furniture furniture = furnitureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid furniture ID: " + id));
        furnitureRepository.delete(furniture);
        return "redirect:/furniture";
    }

    @GetMapping("/search")
    public String searchFurniture(@RequestParam String query, Model model) {
    List<Furniture> searchResults = furnitureRepository.findByNameContainingIgnoreCase(query);
    model.addAttribute("furnitureList", searchResults);
    model.addAttribute("searchQuery", query);
    return "index";
}
}
