package com.example.dbsproj.entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // <-- Import BigDecimal

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private String name;
    private String description;

    // Use BigDecimal to match SQL DECIMAL type
    private BigDecimal price;

    private int stock_quantity;
    private int supplier_id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    public Product() {
    }

    public Product(Integer id, String name, String description, BigDecimal price, int stock_quantity, int supplier_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.supplier_id = supplier_id;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock_quantity() { return stock_quantity; }
    public void setStock_quantity(int stock_quantity) { this.stock_quantity = stock_quantity; }

    public int getSupplier_id() { return supplier_id; }
    public void setSupplier_id(int supplier_id) { this.supplier_id = supplier_id; }

    public ProductCategory getCategory() { return productCategory; }
    public void setCategory(ProductCategory category) { this.productCategory = category; }
}