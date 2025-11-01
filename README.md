# 🛍️ E-Commerce Application (Java Full Stack Development)

## 📖 Overview
This project is a **Spring Boot–based E-Commerce Application** developed under the **Java Full Stack Engineering (FSE)** module.  
It follows a clean, modular architecture ensuring scalability, security, and maintainability.

---

## 📁 Folder Structure

### **1. `com.ecommerce.project`**
- Root package of the application.
- Contains the **main Spring Boot application class**.
- Acts as the base package for component scanning.

---

### **2. `com.ecommerce.project.config`**
- Contains **configuration classes** for setting up beans and frameworks.
- Responsible for:
  - CORS configuration  
  - ModelMapper beans  
  - Other Spring bean definitions

---

### **3. `com.ecommerce.project.controller`**
- Contains all **REST Controller classes**.
- Exposes **API endpoints** for handling HTTP requests and responses.
- Acts as a bridge between frontend and backend.

---

### **4. `com.ecommerce.project.services`**
- Contains **service layer classes**.
- Implements the **business logic** of the application.
- Controllers call these services for data manipulation and processing.

---

### **5. `com.ecommerce.project.repositories`**
- Contains **repository interfaces** that extend `JpaRepository` or `CrudRepository`.
- Responsible for **CRUD operations** and **database interaction**.

---

### **6. `com.ecommerce.project.model`**
- Contains **entity classes** mapped to database tables using JPA annotations.
- Each entity corresponds to a specific table in the database.
- Example: `User`, `Product`, `Order`, etc.

---

### **7. `com.ecommerce.project.payload`**
- Contains **POJOs / DTOs (Data Transfer Objects)**.
- Used for:
  - Receiving input data from client requests.
  - Sending customized response structures to the frontend.

---

### **8. `com.ecommerce.project.security`**
- Contains all **Spring Security** and **JWT (JSON Web Token)** configuration classes.
- Handles:
  - User authentication & authorization  
  - Token generation & validation  
  - Security filters & user details service

---

### **9. `com.ecommerce.project.exception`** *(Optional but Recommended)*
- Contains **custom exception classes** and global exception handling using `@ControllerAdvice`.
- Ensures meaningful and consistent error responses.

---

### **10. `com.ecommerce.project.utils`** *(Optional)*
- Contains **utility/helper classes**.
- Used for reusable functions such as:
  - Token utilities  
  - Email or file operations  
  - Common constants

---


---

## 🧠 Author
**Ankit Dubey**  
Java Full Stack Engineer | Full MERN Stack Developer | DevOps Enthusiast  
