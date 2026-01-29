# Multi-stage Dockerfile for Lucene Analyzer Application

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first for better layer caching
COPY backend/pom.xml ./backend/

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN cd backend && mvn dependency:go-offline -B

# Copy the backend source code
COPY backend/src ./backend/src

# Build the application
RUN cd backend && mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the frontend files first
COPY frontend /app/frontend

# Copy the built JAR from the builder stage
COPY --from=builder /app/backend/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
