# Use the official OpenJDK 17 slim image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container to /app
WORKDIR /app

# Define the argument for the JAR file location; defaults to the built jar in target
ARG JAR_FILE=target/nexusbank-0.0.1-SNAPSHOT.jar

# Copy the JAR file from the host into the container as app.jar
COPY ${JAR_FILE} app.jar

# Expose port 8080 so that the container can be accessed via this port
EXPOSE 8080

# Define the entry point that will run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
