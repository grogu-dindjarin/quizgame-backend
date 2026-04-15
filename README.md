# Quizzle — Backend

Spring Boot REST API for the Quizzle quiz application.

## Prerequisites

- Java 21 (LTS)
- Maven 3.9+
- MySQL 8.0 (or use Docker Compose)

## Getting started

### Option 1 — Docker Compose (recommended)

Start a local MySQL instance:

```bash
docker compose up -d
```

### Option 2 — Existing MySQL

Create a database and update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quizgame?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=<your-user>
spring.datasource.password=<your-password>
```

### Run the application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Running tests

```bash
mvn test
```

Tests use an H2 in-memory database — no MySQL required.

## API overview

| Method | Path                              | Auth     | Description                        |
| ------ | --------------------------------- | -------- | ---------------------------------- |
| POST   | `/api/auth/register`              | Public   | Register a new user                |
| POST   | `/api/auth/login`                 | Public   | Login and receive JWT              |
| GET    | `/api/profile`                    | USER     | Get own profile                    |
| PUT    | `/api/profile`                    | USER     | Update display name and difficulty |
| POST   | `/api/profile/avatar`             | USER     | Upload avatar image                |
| GET    | `/api/categories`                 | Public   | List all categories                |
| POST   | `/api/categories`                 | ADMIN    | Create category                    |
| GET    | `/api/subcategories`              | Public   | List all subcategories             |
| POST   | `/api/subcategories`              | ADMIN    | Create subcategory                 |
| GET    | `/api/questions`                  | Public   | List all questions                 |
| POST   | `/api/questions`                  | ADMIN    | Create question                    |
| POST   | `/api/quiz-attempts`              | USER     | Submit quiz attempt                |
| GET    | `/api/leaderboard`                | USER     | Get leaderboard entries            |
| GET    | `/api/badges`                     | USER     | List all badges                    |
| GET    | `/api/badges/me`                  | USER     | Get badges earned by current user  |

Authentication uses JWT in the `Authorization: Bearer <token>` header.

## Test logins

Two accounts are pre-loaded via `data.sql`:

| Email            | Password | Role  |
| ---------------- | -------- | ----- |
| admin@quizzle.nl  | welkom123 | ADMIN |
| speler@quizzle.nl | welkom123 | USER  |

## Project structure

```
src/main/java/com/bramengel/quizgame/
├── config/         # WebConfig (static resource handlers)
├── controller/     # REST controllers
├── dto/            # Request and response DTOs
├── exception/      # Custom exceptions + GlobalExceptionHandler
├── model/          # JPA entities
├── repository/     # Spring Data JPA repositories
├── security/       # JWT filter, SecurityConfig, UserDetailsServiceImpl
└── service/        # Business logic
```
