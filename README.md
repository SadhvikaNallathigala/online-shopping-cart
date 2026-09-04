# 🛒 Online Shopping Cart   

A backend-based **Online Shopping Cart application** developed using **Java, Spring Boot, Spring Data JPA, Hibernate, and MySQL**. This project implements the core functionality of an e-commerce shopping cart system through RESTful APIs and follows a clean layered architecture.

## 📌 Project Overview   

The Online Shopping Cart is a Spring Boot REST API application that allows products to be managed and added to shopping carts.

The application provides functionality to create, view, update, and delete products, create shopping carts, add products to carts, update quantities, remove cart items, and calculate cart totals.

The project is developed to demonstrate practical backend development concepts including REST APIs, CRUD operations, database integration, JPA/Hibernate, DTOs, validation, exception handling, pagination, and Swagger API documentation.

## ✨ Features

- Product Management
- Shopping Cart Management
- Cart Item Management
- Create, Read, Update, and Delete operations
- Add products to cart
- Update cart item quantities
- Remove products from cart
- View cart details
- Calculate cart total
- MySQL database integration
- Spring Data JPA
- Hibernate ORM
- DTO-based request and response handling
- Input validation
- Exception handling
- Pagination for GET APIs
- Standardized API response structure
- Swagger/OpenAPI documentation
- Maven project management

## 🏗️ Application Architecture

The application follows a layered architecture to maintain separation of concerns and make the application easier to maintain and extend.

```text 
                    Client
                      │
                      ▼
              ┌────────────────┐
              │   Controller   │
              └───────┬────────┘
                      │
                      ▼
              ┌────────────────┐
              │    Service     │
              └───────┬────────┘
                      │
                      ▼
              ┌────────────────┐
              │   Repository   │
              └───────┬────────┘
                      │
                      ▼
              ┌────────────────┐
              │     MySQL      │
              │    Database    │
              └────────────────┘



Controller Layer
The Controller layer handles HTTP requests and responses.
Responsibilities:
- Expose REST endpoints
- Receive client requests
- Pass requests to the service layer
- Return appropriate responses
Service Layer
The Service layer contains the business logic of the application.
Responsibilities:
- Process business operations
- Apply application rules
- Communicate with repositories
- Prepare response data
Repository Layer
The Repository layer handles database operations using Spring Data JPA.
Responsibilities:
- Save data
- Retrieve data
- Update data
- Delete data
- Communicate with MySQL
Entity Layer
The Entity layer represents the application's database tables and their relationships.
DTO Layer
The DTO layer is used to transfer data between the client and application while keeping API models separate from database entities.
🛠️ Technologies Used
Technology	Purpose
Java	Programming Language
Spring Boot	Backend Framework
Spring Data JPA	Database Access
Hibernate	Object Relational Mapping
MySQL	Relational Database
Maven	Build and Dependency Management
Lombok	Boilerplate Code Reduction
REST API	Client-Server Communication
Swagger / OpenAPI	API Documentation and Testing
Git	Version Control
GitHub	Source Code Management


📂 Project Structure
online-shopping-cart/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com.example.onlineshoppingcart/
│       │       ├── controller/
│       │       ├── service/
│       │       ├── repository/
│       │       ├── entity/
│       │       ├── dto/
│       │       └── exception/
│       │
│       └── resources/
│           └── application.properties
│
├── .gitignore
├── pom.xml
└── README.md
🗄️ Database
The application uses MySQL as the relational database.
The main components of the system are:
Product
   │
   ▼
Cart Item
   │
   ▼
Shopping Cart
Product
Represents a product available in the online shopping system.
Product information can include:
- Product ID
- Product Name
- Product Description
- Product Price
- Product Quantity / Stock
Shopping Cart
Represents a shopping cart containing products selected by the customer.
Cart Item
Represents a product added to a shopping cart along with its selected quantity.
🔄 Application Flow
              Start Application
                     │
                     ▼
              Create Product
                     │
                     ▼
            Create Shopping Cart
                     │
                     ▼
             Add Product to Cart
                     │
                     ▼
             Update Quantity
                     │
                     ▼
                View Cart
                     │
                     ▼
             Calculate Total
                     │
                     ▼
            Remove Item if Needed
🚀 Getting Started
Prerequisites
Make sure the following software is installed before running the project:
- Java
- Maven
- MySQL
- Git
- IntelliJ IDEA / VS Code / Eclipse
1. Clone the Repository
git clone https://github.com/SadhvikaNallathigala/online-shopping-cart.git
Navigate to the project directory:
cd online-shopping-cart
2. Configure MySQL
Create a database in MySQL:
CREATE DATABASE online_shopping_cart;
Configure the database connection in:
src/main/resources/application.properties
Example configuration:
spring.datasource.url=jdbc:mysql://localhost:3306/online_shopping_cart
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
Important: Do not commit real database passwords, API keys, or other sensitive information to GitHub.

3. Build the Project
Run the following Maven command:
mvn clean install
4. Run the Application
Start the Spring Boot application:
mvn spring-boot:run
The application will normally run at:
http://localhost:8080
📖 Swagger API Documentation
The project uses Swagger/OpenAPI for API documentation and testing.
After starting the application, open:
http://localhost:8080/swagger-ui/index.html
Swagger provides an interactive interface to:
- View available APIs
- View request parameters
- View request and response structures
- Test REST APIs
- Understand API documentation
🔗 API Operations
Product APIs
POST    /products
GET     /products
GET     /products/{id}
PUT     /products/{id}
DELETE  /products/{id}
Cart APIs
POST    /carts
GET     /carts
GET     /carts/{id}
Cart Item APIs
POST    /carts/{cartId}/items
PUT     /carts/{cartId}/items/{itemId}
DELETE  /carts/{cartId}/items/{itemId}
The exact endpoint paths depend on the controller mappings implemented in the project.

📦 API Response Format
The application follows a standardized API response structure:
{
  "success": true,
  "data": {},
  "error": null,
  "meta": {}
}
Response Fields
Field	Description
success	Indicates whether the operation was successful
data	Contains the requested or processed data
error	Contains error information when an operation fails
meta	Contains additional metadata such as pagination information


🧪 API Testing
The APIs can be tested using:
- Swagger UI
- Postman
- IntelliJ IDEA HTTP Client
- cURL
Example:
curl http://localhost:8080/products
⚠️ Exception Handling
The application handles common backend errors such as:
- Resource not found
- Invalid request data
- Invalid product ID
- Invalid cart ID
- Validation failures
- Database-related exceptions
The application returns appropriate HTTP status codes along with structured error responses.
✅ Validation
Input validation is implemented to ensure that invalid data is not processed by the application.
Validation helps maintain:
- Data integrity
- Consistent API behavior
- Reliable database records
- Better error handling
📊 Pagination
GET APIs that return collections support pagination.
Pagination helps the application:
- Handle large datasets efficiently
- Reduce response size
- Improve API performance
- Avoid loading unnecessary records
- Provide better control over data retrieval
🔐 Security Considerations
The current project focuses on the core shopping cart functionality.
For production-level deployment, additional security features can be implemented, including:
- User authentication
- Authorization
- Password encryption
- JWT-based authentication
- Role-based access control
- Secure database credentials
- HTTPS
🎯 Learning Objectives
This project demonstrates practical implementation of:
- Java backend development
- Spring Boot
- REST API development
- CRUD operations
- Layered architecture
- Spring Data JPA
- Hibernate ORM
- MySQL database integration
- Entity relationships
- DTO-based design
- Input validation
- Exception handling
- Pagination
- Swagger/OpenAPI
- Maven
- Git and GitHub
💡 Key Concepts Demonstrated
The project follows a clear separation of responsibilities:
API Request
     │
     ▼
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
Database
     │
     ▼
Repository
     │
     ▼
Service
     │
     ▼
Controller
     │
     ▼
API Response
This approach makes the application:
- Easy to understand
- Easy to maintain
- Easy to test
- Easy to debug
- Easier to extend
- Scalable for future development
🔮 Future Enhancements
The project can be extended with the following features:
- User Registration and Login
- JWT Authentication
- Role-Based Authorization
- Order Management
- Payment Gateway Integration
- Product Categories
- Product Search and Filtering
- Inventory Management
- Wishlist
- Product Reviews and Ratings
- Coupon and Discount Management
- Email Notifications
- Admin Dashboard
- Frontend Integration
- Cloud Deployment
👩‍💻 Author
Sadhvika Nallathigala
Computer Science Engineering – Artificial Intelligence
GitHub
https://github.com/SadhvikaNallathigala
Project Repository
https://github.com/SadhvikaNallathigala/online-shopping-cart
📄 License
This project is developed for educational and learning purposes.
⭐ If you find this project useful, feel free to explore the repository and provide feedback.
