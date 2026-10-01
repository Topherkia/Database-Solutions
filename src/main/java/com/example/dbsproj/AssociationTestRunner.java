package com.example.dbsproj;

import com.example.dbsproj.entity.Product;
import com.example.dbsproj.entity.ProductCategory;
import com.example.dbsproj.repository.ProductCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class AssociationTestRunner implements CommandLineRunner {

    private final ProductCategoryRepository categoryRepository;

    public AssociationTestRunner(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("\n========== STARTING ASSIGNMENT 11 TEST RUNNER ==========\n");

        // 1. TEST CASCADE PERSISTENCE
        System.out.println("--- 1. Testing Cascade Persistence ---");
        ProductCategory electronics = new ProductCategory(null, "Electronics", "Tech gadgetry and devices");

        Product laptop = new Product(null, "Gaming Laptop", "High performance laptop", BigDecimal.valueOf(1499.99), 10, 101);
        Product phone = new Product(null, "Smartphone", "Latest 5G mobile phone", BigDecimal.valueOf(899.99), 25, 101);

        electronics.addProduct(laptop);
        electronics.addProduct(phone);

        ProductCategory savedCategory = categoryRepository.save(electronics);
        System.out.println("Successfully persisted Category ID: " + savedCategory.getId() + " along with child products.\n");

        // 2. TEST LAZY LOADING
        System.out.println("--- 2. Testing Fetch Strategy (FetchType.LAZY) ---");
        System.out.println("Step A: Querying ProductCategory by ID...");
        
        ProductCategory fetchedCategory = categoryRepository.findById(savedCategory.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        System.out.println("Category fetched: " + fetchedCategory.getName());

        System.out.println("Step B: Accessing child collection getProducts()...");
        
        int count = fetchedCategory.getProducts().size();
        System.out.println("Fetched " + count + " products dynamically via lazy loading.");
        for (Product p : fetchedCategory.getProducts()) {
            System.out.println(" - Item: " + p.getName() + " | Price: $" + p.getPrice());
        }

        System.out.println("\n========== ASSIGNMENT 11 TEST COMPLETED ==========\n");
    }
}