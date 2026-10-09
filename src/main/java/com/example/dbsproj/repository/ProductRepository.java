package com.example.dbsproj.repository;

import com.example.dbsproj.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {

    // Technical Objective 2: JPQL Bulk Update
    @Modifying
    @Query("UPDATE Product p SET p.price = p.price * :multiplier WHERE p.category.id = :categoryId")
    int bulkUpdatePriceByCategory(@Param("categoryId") Integer categoryId, @Param("multiplier") BigDecimal multiplier);

    // Technical Objective 2: JPQL Bulk Delete
    @Modifying
    @Query("DELETE FROM Product p WHERE p.stockQuantity = 0 AND p.category.id = :categoryId")
    int bulkDeleteOutOfStockByCategory(@Param("categoryId") Integer categoryId);
}