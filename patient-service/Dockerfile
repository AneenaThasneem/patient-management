FROM  maven:3.8.5-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn -e dependency:go-offline -B

COPY src ./src
RUN mvn -e clean package
FROM openjdk:17-slim AS runner
WORKDIR /app
COPY --from=builder ./app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "app.jar"]

