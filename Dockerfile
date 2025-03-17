FROM maven:3.8-openjdk-11 as build

# Set working directory
WORKDIR /app

# Copy the POM file
COPY pom.xml .

# Download dependencies (this layer can be cached)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Runtime stage
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/banking-application-service.jar ./app.jar

# Expose the ports
EXPOSE 8080
EXPOSE 1099

# Set environment variables
ENV DATABASE_TIER_URL=https://databasetier.onrender.com
ENV PORT=8080
ENV RMI_PORT=1099

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
