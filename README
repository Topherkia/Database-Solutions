# Database Solutions — Spring Boot Application

This project demonstrates how to build a Java Spring Boot application that accesses a relational database using **Spring Web, Spring Data JPA, and Hibernate**.

The project develops database functionality progressively through Assignments 10–14, covering REST endpoints, entity relationships, JPQL queries, the Criteria API, enum conversion, and entity lifecycle listeners.

## Technologies

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Web** — REST API endpoints
- **Spring Data JPA** — database access and repositories
- **Hibernate** — ORM and entity mapping
- **MariaDB** — relational database
- **Maven** — dependency management and build automation
- **JUnit 5 / Spring Boot Test** — testing

## Features

- REST endpoints for retrieving and creating customers
- Pagination for customer queries
- Product and product-category management
- One-to-many, one-to-one, and many-to-many entity relationships
- Composite primary keys for order items
- JPQL bulk-update operations
- Dynamic queries using the JPA Criteria API
- Enum conversion for order statuses
- JPA entity lifecycle callbacks
- Console-based assignment demonstrations with PowerShell output

## REST API Endpoints

The application runs on `http://localhost:8080` by default.

### Customers

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/customers` | Retrieve a page of customers |
| `GET` | `/customers?page=0&limit=5` | Retrieve five customers from the first page |
| `GET` | `/customers/{id}` | Retrieve a customer by ID |
| `POST` | `/customers` | Create a customer |

Example request to create a customer:

```http
POST http://localhost:8080/customers
Content-Type: application/json
```

```json
{
  "firstName": "John",
  "lastName": "Smith"
}
```

The request body must match the fields and constraints of the `Customer` entity in the project.

### Product Categories

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/categories` | Retrieve all product categories |
| `GET` | `/categories/{id}` | Retrieve a category by ID |
| `POST` | `/categories` | Create a category, optionally with products |
| `DELETE` | `/categories/{id}` | Delete a category |

Example:

```http
GET http://localhost:8080/categories
```

### Association Demonstrations

These endpoints demonstrate entity relationships and fetching associated data.

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/demo/orders/{id}` | Retrieve an order with its products and order items |
| `GET` | `/api/demo/suppliers/{id}` | Retrieve a supplier and its address |

Example:

```http
GET http://localhost:8080/api/demo/orders/1
```

The response includes the order ID, status, and associated products with their quantities and unit prices.

> Note: These endpoints depend on the corresponding records existing in the configured database. The examples above use ID `1` for illustration.

## Assignment Overview

### Assignment 10 — Basic Spring Boot and JPA

Introduces the basic application structure and REST API.

Topics include:

- Spring Boot application configuration
- REST controllers and HTTP methods
- JPA entities and repositories
- Retrieving records by ID
- Pagination using `Pageable` and `PageRequest`
- Creating records through HTTP requests

### Assignment 11 — Entity Relationships

Introduces the relationship between product categories and products.

Topics include:

- `@OneToMany` and `@ManyToOne`
- Entity relationship management
- Cascade persistence
- Lazy loading
- Retrieving categories and their associated products

The category endpoints provide examples of retrieving, creating, and deleting categories.

### Assignment 12 — Advanced Entity Associations

Extends the data model with customers, orders, order items, products, suppliers, and addresses.

Topics include:

- One-to-one relationships between suppliers and addresses
- Many-to-many relationships between orders and products
- Modeling many-to-many relationships through an `OrderItem` entity
- Composite primary keys using `OrderItemId`
- A mapped superclass for shared address attributes
- Fetch joins for retrieving associated data efficiently

The association demonstration endpoints expose selected order and supplier information for testing these mappings.

### Assignment 13 — JPQL and Criteria API

Introduces more advanced database operations.

Topics include:

- JPQL bulk updates
- Bulk operations affecting multiple records
- Deleting records based on conditions
- Criteria API queries
- Dynamic query conditions
- Updating product stock based on price thresholds

Bulk operations require particular care because they can affect many database records at once. Always inspect the query conditions and affected-row count before committing changes.

### Assignment 14 — Enum Conversion and Entity Lifecycle

Introduces additional Hibernate and JPA features.

Topics include:

- Order status represented by an enum
- Conversion between Java enum values and database values
- Custom attribute conversion
- JPA entity lifecycle listeners
- Callbacks such as `@PostLoad`, `@PrePersist`, and `@PreUpdate`

These features demonstrate how entity loading and persistence can trigger application-defined behavior.

## Project Structure

The application uses a package-based structure to separate HTTP handling, database entities, repositories, and assignment demonstrations.

```text
Database-Solutions/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/dbsproj/
│   │   │       ├── DbsprojApplication.java
│   │   │       ├── controller/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── runner/
│   │   │       ├── converter/
│   │   │       └── ...
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

### Main packages

| Package | Responsibility |
|---|---|
| `controller` | Handles HTTP requests and API responses |
| `entity` | Defines Java entities mapped to database tables |
| `repository` | Provides database access through Spring Data JPA |
| `runner` | Contains console-based assignment demonstrations |
| `converter` | Converts Java attribute values, including enums, to database representations |
| `service` | Intended for business logic where applicable |
| `resources` | Stores application configuration |

The precise set of packages may evolve as additional assignments and functionality are implemented.

## Prerequisites

Install the following software before running the project:

- JDK 17 or later, with Java 17 compatibility
- Apache Maven 3.8 or later
- MariaDB Server
- An IDE such as IntelliJ IDEA or Visual Studio Code
- Git

The MariaDB server must be running, and the database schema and tables expected by the application must already exist.

## Setup Instructions

### 1. Clone the repository

```powershell
git clone https://github.com/Topherkia/Database-Solutions.git
cd Database-Solutions
```

Switch to the branch containing the assignment implementations:

```powershell
git fetch origin
git switch --track origin/Project-delivery
```

If you already have a local `Project-delivery` branch, use:

```powershell
git switch Project-delivery
git pull origin Project-delivery
```

### 2. Configure the database connection

Open `src/main/resources/application.properties`.

Configure the JDBC URL, username, and password to match your local MariaDB installation. For example:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/db_sols
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Set the database credentials in PowerShell before launching the application:

```powershell
$env:DB_USERNAME = "your_database_username"
$env:DB_PASSWORD = "your_database_password"
```

Replace the example database name and credentials with the actual values for your environment.

**Important:** The application uses `spring.jpa.hibernate.ddl-auto=none`. This setting prevents Hibernate from automatically generating or updating the schema. Make sure the database schema already matches the entity mappings before running the application.

SQL statements can be displayed in the console by enabling `spring.jpa.show-sql`.

### 3. Build the project

From the project root, run:

```powershell
mvn clean compile
```

Run the test suite with:

```powershell
mvn test
```

### 4. Start the application

```powershell
mvn spring-boot:run
```

Alternatively, open the project in IntelliJ IDEA, locate `DbsprojApplication.java`, and run its `main` method.

When startup succeeds, Spring Boot starts the web server and initializes the application components.

## Running the Assignment Walkthrough

The `AssignmentWalkthroughRunner` is designed to execute selected database demonstrations automatically when the application starts.

It prints section headings, operations, query results, affected-row counts, and errors to the console. This makes it possible to follow the assignment logic directly in PowerShell.

Enable the walkthrough in `application.properties`:

```properties
assignment.demo.enabled=true
```

Then start the application:

```powershell
mvn spring-boot:run
```

The walkthrough covers:

1. **Assignment 10:** Count customers and retrieve a small page of records.
2. **Assignment 11:** Count categories and products and inspect their relationship.
3. **Assignment 12:** Count orders and order items and retrieve an order with its customer.
4. **Assignment 13:** Execute a Criteria API query and demonstrate JPQL and Criteria API bulk updates.
5. **Assignment 14:** Load an order to demonstrate enum conversion and entity lifecycle callbacks.

### Bulk-update safety

By default, the walkthrough is intended to roll back its demonstration bulk updates.

To disable the walkthrough:

```properties
assignment.demo.enabled=false
```

To explicitly opt into committing the demonstrated bulk updates, set the environment variable before starting the application:

```powershell
$env:ASSIGNMENT_DEMO_COMMIT_BULK = "true"
mvn spring-boot:run
```

**Warning:** Committing these operations changes existing product prices and stock quantities. Use this option only when you understand the query conditions and have a suitable backup or test database. The walkthrough does not need to delete records to demonstrate the bulk-delete concept.

Review any other existing startup runners before execution, since they may perform their own database operations.

## Testing the API

You can test the endpoints using a browser, PowerShell, curl, or Postman.

For example, retrieve the first five customers:

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/customers?page=0&limit=5" `
    -Method Get
```

Retrieve a customer by ID:

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/customers/1" `
    -Method Get
```

Retrieve all categories:

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/categories" `
    -Method Get
```

Retrieve an order with its associated products:

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/api/demo/orders/1" `
    -Method Get
```

Use IDs that exist in your database. A `404 Not Found` response is expected when an endpoint cannot find the requested customer, category, order, or supplier.

## Troubleshooting

### Database connection failure

- Confirm that MariaDB is running.
- Verify the JDBC URL, port, database name, username, and password.
- Check that the database and expected tables exist.
- Review the first database-related exception in the Spring Boot console.

### Hibernate mapping or schema errors

- Compare entity table and column mappings against the actual database schema.
- Verify foreign keys, primary keys, and composite-key definitions.
- Keep `ddl-auto=none` when using an existing database; do not switch to `create` to bypass mapping errors.

### Port 8080 is already in use

Check whether another application is using the port, stop that application if appropriate, or configure a different port:

```properties
server.port=8081
```

### Bulk-update operations affect unexpected rows

- Inspect the JPQL or Criteria API conditions.
- Check the reported affected-row count.
- Use a test database or a transaction that is rolled back during demonstrations.
- Do not enable persistent bulk updates until you have reviewed their impact.

## Learning Objectives

This project provides practical experience with:

- Building REST APIs using Spring Boot
- Mapping relational tables to Java objects using JPA and Hibernate
- Designing entity relationships and composite primary keys
- Querying and updating data using repositories, JPQL, and the Criteria API
- Implementing enum converters and entity lifecycle listeners
- Testing API endpoints and interpreting SQL output
- Safely executing database operations against an existing schema

## References

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/index.html)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/reference/)
- [Hibernate ORM Documentation](https://hibernate.org/orm/documentation/)
- [MariaDB Documentation](https://mariadb.com/docs/)
