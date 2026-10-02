package com.example.dbsproj.repository;

import com.example.dbsproj.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Custom query method to find products by category ID
    List<Product> findByCategoryId(Long categoryId);

    // Custom query method to find products associated with a specific supplier
    List<Product> findBySupplierId(Long supplierId);
}