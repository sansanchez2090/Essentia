# ===== STAGE 1: Build =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos del proyecto
COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src

# Compilar sin tests
RUN mvn clean package -DskipTests -B


# ===== STAGE 2: Runtime =====
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Crear usuario no root por seguridad
RUN useradd -m springuser
USER springuser

# Copiar el jar compilado en el stage anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
