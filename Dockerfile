# Stage 1: Build the Spring Boot application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar /app/app.jar

# Set environment variables

# Expose port and run the application
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
