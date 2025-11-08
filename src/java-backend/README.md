# Auth Service - Spring Boot Authentication Microservice

A robust Spring Boot authentication service featuring user management, JWT-based authentication, role-based access control, and user profiles.

## 🚀 Features
- User Registration & Authentication  
- JWT Token-based Security  
- Role-based Access Control (USER, ADMIN, MODERATOR)  
- User Profile Management  
- Password Reset Functionality  
- Docker Containerization  
- Comprehensive Unit Tests  

## 🗄️ Database Configuration
### MySQL Schema
You can find the MySQL schema in the db folder of this same project. 

### Environment Variables
```bash
DB_URL=jdbc:mysql://mysql:3306/auth_db
DB_USER=auth_user
DB_PASS=auth_password
JWT_SECRET=your-super-secure-jwt-secret-key-here
```

## 🔥 REST API Documentation
Base URL: `http://localhost:8081/api`

### Authentication
#### Register User
`POST /auth/register`
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

#### Login
`POST /auth/login`
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

## 🐳 Docker Deployment
### Docker Compose
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: auth_db
      MYSQL_USER: auth_user
      MYSQL_PASSWORD: auth_password
    ports: ["3306:3306"]

  auth-service:
    build: .
    environment:
      DB_URL: jdbc:mysql://mysql:3306/auth_db
      DB_USER: auth_user
      DB_PASS: auth_password
      JWT_SECRET: your-jwt-secret
    ports: ["8081:8081"]
    depends_on:
      - mysql
```

### Dockerfile
```Dockerfile
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 🧠 Development
### Prerequisites
- Java 17
- Maven 3.8+
- MySQL 8.0
- Docker (optional)

### Commands
```bash
mvn spring-boot:run
mvn test
mvn clean package -DskipTests
```

