# Build stage: compile and package the application
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Build the application
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage: create lightweight production image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]