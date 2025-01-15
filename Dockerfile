# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-21-alpine as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven POM file and source code
COPY pom.xml ./
COPY src ./src

# Build the application
RUN mvn clean package

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]