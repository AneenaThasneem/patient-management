FROM maven:3.8.5-openjdk-17-slim AS builder

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build application
COPY src ./src
RUN mvn -e clean package

FROM openjdk:17-slim
WORKDIR /app

# Copy the built jar (using wildcard for version independence)
COPY --from=builder /app/target/billing-service-*.jar app.jar

# Expose ports (HTTP and gRPC)
EXPOSE 4001 9001

# Better JVM options for production
ENTRYPOINT ["java", "-jar", "app.jar"]