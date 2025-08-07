# 📚 Library Management System

A secure, scalable, and modern backend application for managing a library built with **Spring Boot**. The system supports user and book management through REST APIs with advanced features like **JWT-based authentication**, **Redis caching**, **Kafka-driven email notifications**, and **MongoDB Atlas** for cloud-based storage. All APIs are documented using **Swagger UI**.

---

## 🚀 Features

- ✅ RESTful APIs for Users and Books (CRUD)
- 🔐 Authentication & Authorization with JWT + HTTP Basic
- ⚡ Redis caching for performance optimization
- ✉️ Kafka-based email notification system (Producer + Consumer)
- 🌐 API documentation with Swagger/OpenAPI
- ☁️ MongoDB Atlas for cloud-hosted NoSQL database

---

## 🛠️ Tech Stack

| Tech             | Description                                  |
|------------------|----------------------------------------------|
| Spring Boot      | Java backend framework                        |
| MongoDB Atlas    | Cloud NoSQL database                          |
| Spring Security  | Authentication and authorization              |
| JWT              | Token-based secure access                     |
| Redis            | In-memory data store used for caching         |
| Apache Kafka     | Message broker for asynchronous communication |
| Swagger / OpenAPI | API testing and documentation               |
| Maven            | Dependency and build management               |

---

## 📁 API Endpoints Overview

### 🔐 Authentication
- `POST /api/auth/register` → Register new user
- `POST /api/auth/login` → Login and receive JWT token

### 👤 User APIs
- `POST /api/users` → Add new user
- `GET /api/users/{id}` → Get user by ID (cached)
- `GET /api/users/name/{name}` → Get user by name (cached)
- `GET /api/users` → Get all users (cached)
- `PUT /api/users/{id}` → Update user (evicts cache)
- `DELETE /api/users/{id}` → Delete user (evicts cache)
- `POST /api/users/{userId}/issue/{bookId}` → Issue book to user
- `POST /api/users/{userId}/return/{bookId}` → Return issued book

### 📘 Book APIs
- `POST /api/books` → Add book
- `GET /api/books/{id}` → Get book by ID
- `GET /api/books` → Get all books
- `PUT /api/books/{id}` → Update book
- `DELETE /api/books/{id}` → Delete book

---

## 🔒 Authentication Flow

1. Register using `POST /api/auth/register`
2. Login using `POST /api/auth/login`
3. Receive JWT token
4. Add token to request headers:
5. Access protected endpoints

---

## 🧠 MongoDB Atlas Setup

1. Go to [MongoDB Atlas](https://cloud.mongodb.com)
2. Create a cluster and database
3. Whitelist your IP address
4. Create a database user
5. Get the connection URI:

📦 Redis Caching Logic
Cached endpoints:
Get user by ID
Get user by name
Get all users
Cache is cleared on update and delete actions
Example cache keys:
USER::ID::1
USER::NAME::john

📌 Kafka Setup 
Topic: email-notification
Producer: Publishes EmailRequest messages to the topic.
Consumer: Listens to the topic and sends actual emails using the received data.
Broker: Apache Kafka (localhost or remote server)

🌐 Swagger UI
URL: http://localhost:8080/library/v1/swagger-ui/index.html
Features:
Try out API requests
View request/response models
Authorize using JWT

For file structure use this google drive link https://docs.google.com/document/d/1sE1T7Yw--64LJw9WjJRSTiC2WIE6WHKWCa3pI7ZJz2c/edit?usp=drive_link

config/: Configuration classes for MongoDB, Redis, Kafka, Swagger, etc.
controller/: REST controllers.
service/: Business logic.
repository/: Data access layer (MongoDB repositories).
model/: Entity classes.
dto/: Data Transfer Objects.
resources/: Contains application.properties and static/template files.
test/: Unit and integration tests.




