

[![Java](https://img.shields.io/badge/Java-17+-blue)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-green)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-✓-blue)](https://www.docker.com/)
[![LocalStack](https://img.shields.io/badge/LocalStack-2.0-purple)](https://localstack.cloud/)

A production-ready microservice system for patient management featuring:
- REST APIs with Spring Boot
- gRPC for inter-service communication
- Kafka event streaming
- JWT authentication
- Infrastructure-as-Code with CloudFormation
- Local AWS emulation using LocalStack

## 📦 System Architecture

```mermaid
graph TD
    A[API Gateway] --> B[Patient Service]
    A --> C[Auth Service]
    B --> D[(PostgreSQL)]
    B -->|gRPC| E[Billing Service]
    B -->|Kafka| F[Analytics Service]
    C --> G[(Auth DB)]
