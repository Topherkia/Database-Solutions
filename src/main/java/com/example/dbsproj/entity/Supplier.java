package com.example.dbsproj.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_name")
    private String contactName;

    private String phone;
    private String email;

    @OneToOne(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SupplierAddress address;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public SupplierAddress getAddress() { return address; }
    public void setAddress(SupplierAddress address) {
        this.address = address;
        if (address != null) {
            address.setSupplier(this);
        }
    }
}