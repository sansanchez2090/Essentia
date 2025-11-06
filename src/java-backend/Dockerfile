# ===== STAGE 1: Build =====
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Copiar archivos necesarios para compilar
COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B


# ===== STAGE 2: Runtime =====
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copiar solo el jar generado desde la etapa anterior
COPY --from=builder /app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
