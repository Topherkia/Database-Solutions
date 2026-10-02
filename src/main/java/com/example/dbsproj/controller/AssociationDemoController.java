package com.example.dbsproj.controller;

import com.example.dbsproj.entity.Order;
import com.example.dbsproj.entity.OrderItem;
import com.example.dbsproj.entity.Supplier;
import com.example.dbsproj.repository.OrderRepository;
import com.example.dbsproj.repository.SupplierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demo")
public class AssociationDemoController {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;

    public AssociationDemoController(OrderRepository orderRepository, SupplierRepository supplierRepository) {
        this.orderRepository = orderRepository;
        this.supplierRepository = supplierRepository;
    }

    // Example 1: Fetch Order with items and products (N:M Verification)
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderWithProducts(@PathVariable Long id) {
        return orderRepository.findByIdWithProducts(id)
                .map(order -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("orderId", order.getId());
                    response.put("status", order.getStatus());
                    
                    List<Map<String, Object>> items = order.getItems().stream().map(item -> {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("productName", item.getProduct().getName());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("unitPrice", item.getUnitPrice());
                        return itemMap;
                    }).collect(Collectors.toList());

                    response.put("items", items);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Example 2: Fetch Supplier with address inherited properties (1:1 & Inheritance Verification)
    @GetMapping("/suppliers/{id}")
    public ResponseEntity<?> getSupplierWithAddress(@PathVariable Long id) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("supplierId", supplier.getId());
                    response.put("name", supplier.getName());
                    
                    if (supplier.getAddress() != null) {
                        Map<String, Object> addressMap = new HashMap<>();
                        addressMap.put("streetAddress", supplier.getAddress().getStreetAddress());
                        addressMap.put("city", supplier.getAddress().getCity());
                        addressMap.put("postalCode", supplier.getAddress().getPostalCode());
                        addressMap.put("country", supplier.getAddress().getCountry());
                        response.put("address", addressMap);
                    }
                    
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}