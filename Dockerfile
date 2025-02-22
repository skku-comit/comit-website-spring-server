# Use the official OpenJDK image as a base image
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle settings.gradle /app/

# Copy the gradle wrapper files
COPY gradlew /app/
COPY gradle /app/gradle

# Download the dependencies
RUN ./gradlew build

# Copy the project files
COPY . /app

# Build the application
RUN ./gradlew build

# Expose the port the app runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "build/libs/comit-server-0.0.1-SNAPSHOT.jar"]