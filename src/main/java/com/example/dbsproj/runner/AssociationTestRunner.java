package com.example.dbsproj.runner;

import com.example.dbsproj.entity.*;
import com.example.dbsproj.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class AssociationTestRunner implements CommandLineRunner {

    private final SupplierRepository supplierRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public AssociationTestRunner(
        SupplierRepository supplierRepository,
        OrderRepository orderRepository,
        ProductRepository productRepository,
        CustomerRepository customerRepository) {

        this.supplierRepository = supplierRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("==================================================");
        System.out.println("   RUNNING JPA ASSOCIATION & INHERITANCE TESTS   ");
        System.out.println("==================================================");

        testOneToOneAndInheritance();
        testManyToMany();

        System.out.println("==================================================");
        System.out.println("          ALL TESTS COMPLETED SUCCESSFULLY         ");
        System.out.println("==================================================");
    }

    private void testOneToOneAndInheritance() {
        System.out.println("\n--- [1] Testing 1:1 Relationship & MappedSuperclass Inheritance ---");

        // 1. Create Supplier
        Supplier supplier = new Supplier();
        supplier.setName("TechCorp Logistics");
        supplier.setContactName("Alice Smith");
        supplier.setEmail("contact@techcorp.com");
        supplier.setPhone("+1-555-0199");

        // 2. Create SupplierAddress (inherits streetAddress, postalCode, city, country from BaseAddress)
        SupplierAddress address = new SupplierAddress();
        address.setStreetAddress("123 Innovation Way");
        address.setPostalCode("90210");
        address.setCity("Tech City");
        address.setCountry("USA");

        // 3. Link 1:1 bidirectional relationship
        supplier.setAddress(address);

        // 4. Save Supplier (Cascades to SupplierAddress)
        Supplier savedSupplier = supplierRepository.save(supplier);
        System.out.println("Saved Supplier ID: " + savedSupplier.getId());

        // 5. Retrieve Supplier and verify inherited address properties
        Supplier retrievedSupplier = supplierRepository.findById(savedSupplier.getId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        System.out.println("Retrieved Supplier: " + retrievedSupplier.getName());
        System.out.println("Retrieved Address (via BaseAddress inheritance): " 
                + retrievedSupplier.getAddress().getStreetAddress() + ", "
                + retrievedSupplier.getAddress().getCity() + ", "
                + retrievedSupplier.getAddress().getCountry());

        // 6. Clean up test data
        supplierRepository.delete(retrievedSupplier);
        System.out.println("Deleted test Supplier (and cascaded address).");
    }

    private void testManyToMany() {
        System.out.println("\n--- [2] Testing N:M Relationship (Order <-> OrderItem <-> Product) ---");

        // 1. Fetch an existing product or create a temporary test product
        Product product = new Product();
        product.setName("Test Wireless Mouse");
        product.setPrice(new BigDecimal("29.99"));
        product.setStockQuantity(100);
        Product savedProduct = productRepository.save(product);

        // 2. Fetch an existing customer
        Customer customer = customerRepository.findAll()
        .stream()
        .findFirst()
        .orElseThrow(() -> new RuntimeException("No customer found"));

        // 3. Create Order
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");

        // 4. Create OrderItem (Join table payload entity)
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(savedProduct);
        orderItem.setQuantity(2);
        orderItem.setUnitPrice(savedProduct.getPrice());

        // Add item to order
        order.addItem(orderItem);

        // 4. Save Order (Cascades to OrderItem)
        Order savedOrder = orderRepository.save(order);
        System.out.println("Saved Order ID: " + savedOrder.getId());

        // 5. Retrieve Order and verify N:M relationship
        Order retrievedOrder = orderRepository.findByIdWithProducts(savedOrder.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        System.out.println("Retrieved Order ID: " + retrievedOrder.getId() + " | Status: " + retrievedOrder.getStatus());
        retrievedOrder.getItems().forEach(item -> {
            System.out.println(" -> Purchased Product: " + item.getProduct().getName() 
                    + " | Qty: " + item.getQuantity() 
                    + " | Unit Price: $" + item.getUnitPrice());
        });

        // 6. Clean up test data
        orderRepository.delete(retrievedOrder);
        productRepository.delete(savedProduct);
        System.out.println("Deleted test Order and Product.");
    }
}