# Build stage: compile and package the application
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Download dependencies first (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build the application
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage: create lightweight production image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9000
ENTRYPOINT ["java","-jar","app.jar"]