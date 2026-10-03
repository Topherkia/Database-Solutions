package com.example.dbsproj.repository;

import com.example.dbsproj.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    // Technical Objective 3: Criteria Update
    @Override
    public int bulkUpdateStockByPriceThreshold(BigDecimal priceThreshold, int stockIncrease) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Product> update = cb.createCriteriaUpdate(Product.class);
        Root<Product> root = update.from(Product.class);

        // Fix: Explicitly declare the integer path to resolve method signature ambiguity
        Path<Integer> stockPath = root.get("stockQuantity");
        update.set(stockPath, cb.sum(stockPath, stockIncrease));
        
        update.where(cb.greaterThan(root.get("price"), priceThreshold));

        return entityManager.createQuery(update).executeUpdate();
    }

    // Technical Objective 3: Dynamic Criteria Query
    @Override
    public List<Product> findProductsByDynamicFilter(String namePattern, BigDecimal minPrice, Integer categoryId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (namePattern != null && !namePattern.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + namePattern.toLowerCase() + "%"));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (categoryId != null) {
            predicates.add(cb.equal(root.get("category").get("id"), categoryId));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}