package com.example.dbsproj.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "supplieraddresses")
public class SupplierAddress extends BaseAddress {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier supplier;

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
}
