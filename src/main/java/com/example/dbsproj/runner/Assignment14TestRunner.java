package com.example.dbsproj.runner;

import com.example.dbsproj.entity.Customer;
import com.example.dbsproj.entity.Order;
import com.example.dbsproj.repository.CustomerRepository;
import com.example.dbsproj.repository.OrderRepository;

import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Assignment14TestRunner implements CommandLineRunner {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    // Spring automatically injects dependencies when a class has a single constructor
    public Assignment14TestRunner(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        runConverterTest();
    }

    private void runConverterTest() {
        System.out.println("======= Assignment 14 Test Runner: Converter Test =======");
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("User");
        customer.setEmail("test@example.com");
        customer = customerRepository.save(customer);

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        System.out.println(" Order saved with status: " + order.getStatus());
        System.out.println("======= Assignment 14 Completed =======");
    }
}