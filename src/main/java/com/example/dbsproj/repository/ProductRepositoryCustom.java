package com.example.dbsproj.repository;

import com.example.dbsproj.entity.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepositoryCustom {
    // Technical Objective 3: Criteria API Bulk Update
    int bulkUpdateStockByPriceThreshold(BigDecimal priceThreshold, int stockIncrease);
    
    // Technical Objective 3: Dynamic Criteria API Query
    List<Product> findProductsByDynamicFilter(String namePattern, BigDecimal minPrice, Integer categoryId);
}