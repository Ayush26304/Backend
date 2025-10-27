# 🛒 KartCom E-Commerce Backend

KartCom is a microservices-based e-commerce backend built using **Spring Boot**. It provides core functionalities such as product catalog management, cart operations, order processing, and user authentication.

---

## 📦 Modules

- **User Service** – Handles user registration, login, and JWT authentication  
- **Product Service** – Manages product listings and stock levels  
- **Cart Service** – Allows users to add/remove items and view their cart  
- **Order Service** – Processes orders and tracks order history  
- **Gateway Service** – Routes API requests and enforces security  
- **Discovery Service** – Uses Eureka for service registration and discovery  

---

## 🚀 Technologies Used

- Spring Boot  
- Spring Cloud (OpenFeign, Eureka, Gateway)  
- Spring Security + JWT  
- MySQL / PostgreSQL  
- Maven  
- Lombok  

---

## 🧰 Setup Instructions

### Prerequisites
- Java 17+  
- Maven  
- MySQL or PostgreSQL  
- Eureka Server (for service discovery)  

### Steps
# Clone the repository
git clone https://github.com/Ayush26304/Backend.git
cd Backend

# Build the project
mvn clean install

# Run individual services
cd cart-service
mvn spring-boot:run

cd product-service
mvn spring-boot:run
# Repeat for other services
