
package com.example.dbsproj.runner;

import com.example.dbsproj.entity.Customer;
import com.example.dbsproj.entity.Order;
import com.example.dbsproj.entity.Product;
import com.example.dbsproj.entity.ProductCategory;
import com.example.dbsproj.repository.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(
    name = "assignment.demo.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class AssignmentWalkthroughRunner implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository productRepository;
    private final TransactionTemplate transactionTemplate;

    @Value("${ASSIGNMENT_DEMO_COMMIT_BULK:false}")
    private boolean commitBulkUpdates;

    public AssignmentWalkthroughRunner(
            ProductRepository productRepository,
            PlatformTransactionManager transactionManager) {
        this.productRepository = productRepository;
        this.transactionTemplate =
                new TransactionTemplate(transactionManager);
    }

    @Override
    public void run(String... args) {
        line("SPRING BOOT DATABASE ASSIGNMENT WALKTHROUGH");

        System.out.println("[CONFIG] Hibernate schema changes are disabled.");
        System.out.println("[CONFIG] Bulk updates will "
                + (commitBulkUpdates
                ? "COMMIT."
                : "ROLL BACK after demonstration."));

        assignment10();
        assignment11();
        assignment12();
        assignment13();
        assignment14();

        line("ALL ASSIGNMENT DEMONSTRATIONS FINISHED");
    }

    // Assignment 10: basic JPA queries and pagination
    private void assignment10() {
        section("ASSIGNMENT 10 - BASIC JPA QUERIES");

        safely("Count customers", () -> {
            Long count = entityManager.createQuery(
                    "SELECT COUNT(c) FROM Customer c",
                    Long.class
            ).getSingleResult();

            System.out.println("[A10] Customer count: " + count);
        });

        safely("Read a small page of customers", () -> {
            List<Customer> customers = entityManager.createQuery(
                    "SELECT c FROM Customer c ORDER BY c.id",
                    Customer.class
            ).setMaxResults(5).getResultList();

            System.out.println("[A10] Sample customer records:");

            customers.forEach(c -> System.out.println(
                    "  id=" + c.getId()
                    + ", name=" + c.getFirstName()
                    + " " + c.getLastName()
            ));
        });
    }

    // Assignment 11: ProductCategory and Product relationship
    private void assignment11() {
        section("ASSIGNMENT 11 - ONE-TO-MANY RELATIONSHIP");

        safely("Count categories and products", () -> {
            Long categories = entityManager.createQuery(
                    "SELECT COUNT(c) FROM ProductCategory c",
                    Long.class
            ).getSingleResult();

            Long products = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Product p",
                    Long.class
            ).getSingleResult();

            System.out.println("[A11] Categories: " + categories);
            System.out.println("[A11] Products: " + products);
        });

        safely("Read categories with product counts", () -> {
            List<Object[]> rows = entityManager.createQuery(
                    "SELECT c.id, c.name, COUNT(p) "
                    + "FROM ProductCategory c "
                    + "LEFT JOIN c.products p "
                    + "GROUP BY c.id, c.name "
                    + "ORDER BY c.id",
                    Object[].class
            ).setMaxResults(5).getResultList();

            rows.forEach(row -> System.out.println(
                    "  categoryId=" + row[0]
                    + ", name=" + row[1]
                    + ", products=" + row[2]
            ));
        });
    }

    // Assignment 12: Customer, Order, and OrderItem associations
    private void assignment12() {
        section("ASSIGNMENT 12 - ORDER ASSOCIATIONS");

        safely("Count orders and order items", () -> {
            Long orders = entityManager.createQuery(
                    "SELECT COUNT(o) FROM Order o",
                    Long.class
            ).getSingleResult();

            Long items = entityManager.createQuery(
                    "SELECT COUNT(i) FROM OrderItem i",
                    Long.class
            ).getSingleResult();

            System.out.println("[A12] Orders: " + orders);
            System.out.println("[A12] Order items: " + items);
        });

        safely("Fetch one order and its customer", () -> {
            List<Order> orders = entityManager.createQuery(
                    "SELECT o FROM Order o "
                    + "JOIN FETCH o.customer "
                    + "ORDER BY o.id",
                    Order.class
            ).setMaxResults(1).getResultList();

            if (orders.isEmpty()) {
                System.out.println("[A12] No orders found.");
                return;
            }

            Order order = orders.get(0);

            System.out.println(
                    "[A12] Order id=" + order.getId()
                    + ", customerId=" + order.getCustomer().getId()
                    + ", status=" + order.getStatus()
            );
        });
    }

    // Assignment 13: JPQL bulk operations and Criteria API
    private void assignment13() {
        section("ASSIGNMENT 13 - JPQL AND CRITERIA API");

        safely("Run a dynamic Criteria API query", () -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query =
                    cb.createQuery(Product.class);

            Root<Product> product = query.from(Product.class);

            List<Predicate> conditions = new ArrayList<>();
            conditions.add(cb.isNotNull(product.get("name")));
            conditions.add(
                    cb.greaterThanOrEqualTo(
                            product.<BigDecimal>get("price"),
                            BigDecimal.ZERO
                    )
            );

            query.select(product)
                    .where(conditions.toArray(new Predicate[0]))
                    .orderBy(cb.asc(product.get("id")));

            List<Product> products = entityManager
                    .createQuery(query)
                    .setMaxResults(5)
                    .getResultList();

            System.out.println(
                    "[A13] Criteria API returned "
                    + products.size() + " sample products."
            );

            products.forEach(p -> System.out.println(
                    "  id=" + p.getId()
                    + ", name=" + p.getName()
                    + ", price=" + p.getPrice()
            ));
        });

        safely("Demonstrate bulk updates in a transaction", () -> {
            transactionTemplate.execute(status -> {
                List<Integer> categoryIds = entityManager.createQuery(
                        "SELECT c.id FROM ProductCategory c ORDER BY c.id",
                        Integer.class
                ).setMaxResults(1).getResultList();

                if (categoryIds.isEmpty()) {
                    System.out.println(
                            "[A13] No categories found; updates skipped."
                    );
                    status.setRollbackOnly();
                    return null;
                }

                Integer categoryId = categoryIds.get(0);

                System.out.println(
                        "[A13] Executing JPQL bulk price update: +1% "
                        + "for category " + categoryId
                );

                int priceRows = entityManager.createQuery(
                        "UPDATE Product p "
                        + "SET p.price = p.price * 1.01 "
                        + "WHERE p.category.id = :categoryId"
                ).setParameter("categoryId", categoryId)
                 .executeUpdate();

                System.out.println(
                        "[A13] JPQL update affected "
                        + priceRows + " row(s)."
                );

                System.out.println(
                        "[A13] Executing Criteria API stock update: "
                        + "increase stock by 1 for products priced >= 100."
                );

                int stockRows =
                        productRepository.bulkUpdateStockByPriceThreshold(
                                new BigDecimal("100.00"), 1
                        );

                System.out.println(
                        "[A13] Criteria API update affected "
                        + stockRows + " row(s)."
                );

                if (commitBulkUpdates) {
                    System.out.println(
                            "[A13] Explicit commit enabled; updates will persist."
                    );
                } else {
                    status.setRollbackOnly();
                    System.out.println(
                            "[A13] ROLLBACK requested; updates will not persist."
                    );
                }

                return null;
            });
        });

        safely("Preview bulk-delete candidates without deleting", () -> {
            Long count = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Product p "
                    + "WHERE p.stockQuantity = 0",
                    Long.class
            ).getSingleResult();

            System.out.println(
                    "[A13] Out-of-stock products: " + count
                    + ". No rows were deleted."
            );
        });
    }

    // Assignment 14: enum converter and JPA entity lifecycle listener
    private void assignment14() {
        section("ASSIGNMENT 14 - CONVERTER AND ENTITY LISTENER");

        safely("Load an order and inspect its status", () -> {
            List<Integer> ids = entityManager.createQuery(
                    "SELECT o.id FROM Order o ORDER BY o.id",
                    Integer.class
            ).setMaxResults(1).getResultList();

            if (ids.isEmpty()) {
                System.out.println("[A14] No orders found.");
                return;
            }

            Order order = entityManager.find(Order.class, ids.get(0));

            System.out.println(
                    "[A14] Order id=" + order.getId()
                    + ", converted status=" + order.getStatus()
            );

            System.out.println(
                    "[A14] The @PostLoad listener should also log the load."
            );
        });
    }

    private void safely(String operation, Runnable action) {
        try {
            System.out.println();
            System.out.println("[RUN] " + operation);
            action.run();
        } catch (Exception ex) {
            System.err.println(
                    "[WARN] " + operation + " failed: "
                    + ex.getClass().getSimpleName()
                    + ": " + ex.getMessage()
            );
            System.err.println(
                    "[WARN] Continuing to the next demonstration."
            );
        }
    }

    private void section(String title) {
        System.out.println();
        line(title);
    }

    private void line(String message) {
        System.out.println(
                "============================================================"
        );
        System.out.println(message);
        System.out.println(
                "============================================================"
        );
    }
}
