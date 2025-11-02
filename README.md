# ⚙️ O2 Shop Backend

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge&logo=springboot)
![Render](https://img.shields.io/badge/Deployed%20on-Render-blue?style=for-the-badge&logo=render)
![Database](https://img.shields.io/badge/Database-PostgreSQL-blue?style=for-the-badge&logo=postgresql)
![Build](https://img.shields.io/badge/Build-Success-success?style=for-the-badge)

> 💡 **O2 Shop Backend** — RESTful API powering the O2 Shop E-Commerce Platform, built using **Spring Boot**, **JPA**, **PostgreSQL**, and **JWT-based Authentication**.  
> Fully deployed and production-ready on **Render**.

---

## 🚀 Live API  
🔗 **Base URL:** [https://o2-shop-backend.onrender.com](https://o2-shop-backend.onrender.com)  
📡 Example Endpoint:  
GET /api/products

yaml
Copy code

---

## 🧩 Tech Stack

| Layer | Technology |
|-------|-------------|
| Backend Framework | Spring Boot 3.x |
| ORM | Hibernate / JPA |
| Database | PostgreSQL |
| Auth | JWT + Cookies |
| Build Tool | Maven |
| Cloud Hosting | Render |
| CORS | Configured for Vercel Frontend |

---

## ⚙️ Key Features

✅ JWT + Cookie-based Authentication  
✅ Role-based Access (USER / SELLER / ADMIN)  
✅ Product CRUD APIs  
✅ Image Uploads (`BYTEA` storage in DB)  
✅ Secure CORS setup for frontend integration  
✅ Deployed seamlessly on Render  
✅ Auto Schema Update with Hibernate  

---

## 🔐 Authentication Flow

1. User logs in via `/auth/login`  
2. Server validates credentials  
3. JWT token & role stored in cookies  
4. React frontend reads role → redirects to correct dashboard  
5. Cookies automatically sent with `withCredentials: true`  

---

## 🧠 API Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/auth/signup` | Register a new user |
| `POST` | `/auth/login` | Login user and issue JWT cookie |
| `GET`  | `/products` | Get all products |
| `POST` | `/products/add` | Add new product (Seller only) |
| `POST` | `/product-photo/upload` | Upload product image |
| `GET`  | `/auth/me` | Verify login & get current user |
