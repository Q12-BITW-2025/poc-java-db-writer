# syntax=docker/dockerfile:1

# === Stage 1: Build the Spring Boot app ===
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper & files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies first (cached)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src src

# Build the application JAR (skip tests for speed)
RUN ./mvnw clean package -DskipTests

# === Stage 2: Run with minimal JDK 21 ===
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port if you have REST endpoints
EXPOSE 8080

# Don't hardcode JAVA_TOOL_OPTIONS — Odigos will inject it dynamically!
ENTRYPOINT ["java","-jar","/app/app.jar"]
