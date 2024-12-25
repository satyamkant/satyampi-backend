# Use the official OpenJDK 23 image as a base image
FROM openjdk:17-jdk-slim as build

# Set working directory in the container
WORKDIR /app

# Copy the built jar file from the Jenkins build step
COPY target/SecurityMs-0.0.1-SNAPSHOT.jar /app/SecurityMs.jar

# Expose the port the application will run on (adjust according to your app's port)
EXPOSE 2000

# Run the application
CMD ["java", "-jar", "/app/SecurityMs.jar"]