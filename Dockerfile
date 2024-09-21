# Step 1: Use a base image with Java
FROM eclipse-temurin:17

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the application JAR file from the host to the container
COPY target/springboot-blog-rest-api-0.0.1-SNAPSHOT.jar blog-app.jar

# Step 4: Expose the port your app will run on
EXPOSE 8080

# Step 5: Command to run the application
ENTRYPOINT ["java", "-jar", "blog-app.jar"]
