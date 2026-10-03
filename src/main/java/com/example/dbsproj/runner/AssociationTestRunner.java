package com.example.dbsproj.runner;

import com.example.dbsproj.entity.Product;
import com.example.dbsproj.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AssociationTestRunner implements CommandLineRunner {

    private final ProductRepository productRepository;

    public AssociationTestRunner(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== STARTING ASSIGNMENT 13 BULK OPERATIONS TEST ===");

        // 1. JPQL Bulk Update
        System.out.println("\n--- JPQL Bulk Update Test ---");
        BigDecimal multiplier = new BigDecimal("1.10"); // 10% price increase
        int updatedCount = productRepository.bulkUpdatePriceByCategory(1, multiplier);
        System.out.println("JPQL Bulk Update: Updated " + updatedCount + " products in Category 1.");

        // 2. Criteria API Bulk Update
        System.out.println("\n--- Criteria API Bulk Update Test ---");
        int criteriaUpdated = productRepository.bulkUpdateStockByPriceThreshold(new BigDecimal("100.00"), 50);
        System.out.println("Criteria API Bulk Update: Updated stock for " + criteriaUpdated + " products.");

        // 3. Dynamic Criteria Query
        System.out.println("\n--- Dynamic Criteria Query Test ---");
        List<Product> filteredProducts = productRepository.findProductsByDynamicFilter("pro", new BigDecimal("50.00"), 1);
        System.out.println("Found " + filteredProducts.size() + " products matching criteria.");

        System.out.println("\n=== ASSIGNMENT 13 TESTS COMPLETED ===");
    }
}