# Use the official OpenJDK image as a base image
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the gradle configuration files first
COPY build.gradle settings.gradle /app/
COPY gradlew /app/
COPY gradle /app/gradle

# Make the gradlew executable
RUN chmod +x ./gradlew

# Download dependencies only (using a placeholder main class)
RUN ./gradlew dependencies --no-daemon

# Now copy the source code
COPY src /app/src

# Build the application
RUN ./gradlew build --no-daemon

# Expose the port the app runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "build/libs/comit-server-0.0.1-SNAPSHOT.jar"]