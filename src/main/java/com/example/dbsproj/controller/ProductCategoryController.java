package com.example.dbsproj.controller;

import com.example.dbsproj.entity.Product;
import com.example.dbsproj.entity.ProductCategory;
import com.example.dbsproj.repository.ProductCategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class ProductCategoryController {

    private final ProductCategoryRepository categoryRepository;

    public ProductCategoryController(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<ProductCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Integer id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductCategory> createCategoryWithProducts(@RequestBody ProductCategory category) {
        if (category.getProducts() != null) {
            for (Product p : category.getProducts()) {
                p.setCategory(category);
            }
        }
        ProductCategory savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}