package com.example.dbsproj.listener;

import com.example.dbsproj.entity.Order;
import jakarta.persistence.*;

public class OrderLifecycleListener {

    @PostLoad
    public void onPostLoad(Order order) {
        // Cache initial status for comparison during updates
        System.out.println("[PostLoad] Loaded Order ID " + order.getId() + " with status: " + order.getStatus());
    }

    @PrePersist
    public void onPrePersist(Order order) {
        if (order.getCustomer() != null) {
            Integer customerId = order.getCustomer().getId();
        
        System.out.println("[PrePersist] Preparing to persist Order for customer: " + customerId);
        } else {
            System.out.println("[PrePersist] Preparing to persist Order with no customer set.");
        }
    }

    @PreUpdate
    public void onPreUpdate(Order order) {
        System.out.println("[PreUpdate] Order ID " + order.getId() + " updating to status: " + order.getStatus());
    }
}
