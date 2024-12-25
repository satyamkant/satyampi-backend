# Use the official OpenJDK 23 image as a base image
FROM openjdk:17-jdk-slim as build

# Set working directory in the container
WORKDIR /app

# Copy the built jar file from the Jenkins build step
COPY target/UserMs-0.0.1-SNAPSHOT.jar /app/UserMs.jar

# Expose the port the application will run on (adjust according to your app's port)
EXPOSE 2003

# Run the application
CMD ["java", "-jar", "/app/UserMs.jar"]