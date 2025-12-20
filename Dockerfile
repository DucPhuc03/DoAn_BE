# =========================
# STAGE 1: Build JAR
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /build

# Copy pom trước để cache dependency
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests


# =========================
# STAGE 2: Run app
# =========================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy jar từ stage build
COPY --from=build /build/target/*.jar app.jar

# Expose port Spring Boot
EXPOSE 8080

# Chạy app
ENTRYPOINT ["java","-jar","app.jar"]
