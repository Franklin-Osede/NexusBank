# NexusBank

NexusBank is a modular and scalable banking application built using Java and Spring Boot. It implements the principles of **Hexagonal Architecture** (Ports & Adapters) to ensure that the core business logic remains isolated from external technical concerns. The project is developed using **Test-Driven Development (TDD)**, which guarantees robust, maintainable, and well-tested code. Additionally, NexusBank integrates a compiled Truffle smart contract for blockchain interactions and includes configurations for Docker and Kubernetes deployments.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Hexagonal Architecture](#hexagonal-architecture)
- [Test-Driven Development (TDD) Approach](#test-driven-development-tdd-approach)
  - [Development Phases](#development-phases)
  - [Red-Green-Refactor Cycle](#red-green-refactor-cycle)
- [Running and Testing the Application](#running-and-testing-the-application)
- [Deployment and Configuration](#deployment-and-configuration)
  - [Docker Setup](#docker-setup)
  - [Kubernetes Deployment](#kubernetes-deployment)
- [Best Practices & Considerations](#best-practices--considerations)
- [Contact and Support](#contact-and-support)

---

## Project Overview

NexusBank is designed to offer a robust, secure, and scalable solution for banking operations. The application handles tasks such as creating user accounts, processing deposits, transferring funds, and managing transactions. It leverages Spring Boot for rapid development and provides a RESTful API for interacting with its core services. In addition to traditional banking operations, NexusBank incorporates a blockchain component through a compiled Truffle smart contract to facilitate secure transaction validations and other decentralized operations.

---

## Hexagonal Architecture

The project follows the **Hexagonal Architecture** pattern, which organizes the application into several layers to clearly separate concerns:

- **Core Domain**: Contains the business rules, models, and exceptions. This layer defines the essential logic for accounts, users, transactions, and monetary operations.
- **Application Services and Ports**: Exposes use cases through input and output interfaces. This abstraction allows for easier testing and adaptability, ensuring that the business logic is not coupled with external systems.
- **Infrastructure**: Implements technical details such as persistence, REST controllers, configuration, and external integrations. This layer interacts with databases, maps domain models to persistence entities, and exposes the API.

This separation enables developers to test the core functionality independently, simplifies maintenance, and allows for seamless integration with external systems.

---

## Test-Driven Development (TDD) Approach

NexusBank is built following the **TDD** methodology, ensuring that tests define the expected behavior before implementation. This approach leads to a reliable, high-coverage codebase and facilitates early detection of issues.

### Development Phases

1. **Initial Project Setup**  
   - **Function**: Establish the foundational project configuration to support development.
   - **Key Files**:  
     - `pom.xml`: Manages Maven dependencies and build configurations.
     - `application.properties`: Sets up Spring Boot configurations such as database connections and server properties.
     - The main Spring Boot class: Bootstraps the application.
   - **TDD Importance**: Even though initial configuration is not directly test-driven, it is critical to establish a stable environment for running tests.

2. **Domain Implementation**  
   - **Function**: Create the core business logic independent of external factors.
   - **Key Areas**:  
     - **Money**: Value object representing monetary values.
     - **Account**: Entity representing bank accounts.
     - **Transaction**: Entity managing transaction details along with its status and type.
     - **User**: Entity representing bank customers.
     - **Domain Exceptions**: Custom exceptions for scenarios like insufficient balance or not found accounts/users.
   - **TDD Importance**: Write tests for each domain model and exception first, then implement the minimal code needed for tests to pass. This ensures that business rules are correctly enforced.

3. **Ports (Interfaces) Implementation**  
   - **Function**: Define the contracts (input and output interfaces) that connect the domain to the external world.
   - **Key Areas**:  
     - **Input Ports**: Define use cases such as creating users, depositing money, transferring funds, etc.
     - **Output Ports**: Specify interfaces for persistence operations such as loading or saving accounts, users, and transactions.
   - **TDD Importance**: By defining these interfaces upfront, tests can drive the implementation of application services and infrastructure adapters.

4. **Application Services Development**  
   - **Function**: Implement the business use cases by orchestrating domain operations through the defined ports.
   - **Key Areas**:  
     - Services for account management, transaction processing, and user operations.
   - **TDD Importance**: Develop tests that simulate interactions using mocks for outbound interfaces. Write just enough service logic to pass the tests, then refactor for clarity and efficiency.

5. **Persistence Adapters**  
   - **Function**: Implement data access logic using JPA, mapping domain entities to database tables.
   - **Key Areas**:  
     - JPA Entities and Mappers: Convert between domain models and persistence entities.
     - Spring Data Repositories: Provide a layer for data operations.
     - Persistence Adapters: Implement outbound port interfaces to interact with the database.
     - Integration Tests: Use TestContainers to simulate real database environments.
   - **TDD Importance**: Write tests for persistence adapters before implementation to verify correct integration with the database and mapping logic.

6. **REST API and Adapters**  
   - **Function**: Expose the application’s functionality via a RESTful API.
   - **Key Areas**:  
     - DTOs: Define data structures for API requests and responses.
     - Controllers: Map HTTP requests to application services.
     - Configuration: Setup security and OpenAPI documentation.
   - **TDD Importance**: Create tests for controllers to ensure correct HTTP response handling, then implement endpoints that satisfy these tests.

7. **End-to-End Integration Tests**  
   - **Function**: Ensure that all layers of the application work together seamlessly.
   - **Key Areas**:  
     - API tests that validate the complete flow from HTTP requests to database persistence.
     - Use of TestContainers to emulate a production-like environment.
   - **TDD Importance**: Comprehensive integration tests confirm that the system behaves as expected under realistic conditions.

8. **Deployment Configurations**  
   - **Function**: Prepare the application for deployment in various environments.
   - **Key Areas**:  
     - Docker: Containerization of the application.
     - Kubernetes: Deployment configurations for orchestration.
   - **TDD Importance**: While not directly test-driven, ensuring deployment configurations work is vital for production readiness.

9. **Documentation**  
   - **Function**: Provide detailed information on how to use, develop, and deploy the application.
   - **Key Areas**:  
     - This README file.
     - OpenAPI configurations for API documentation.
   - **TDD Importance**: Clear documentation supports the development process and helps new contributors understand the system architecture and testing approach.

### Red-Green-Refactor Cycle

- **Red**: Write a failing test that clearly defines the expected behavior.
- **Green**: Implement the minimal code necessary to pass the test.
- **Refactor**: Improve the code’s structure and quality while ensuring all tests continue to pass.
- **Repeat**: Continuously iterate through these phases for each new feature or bug fix.

---

## Running and Testing the Application

### Running the Application

**Using Maven**  

   Navigate to the project root directory and execute:
   ```bash
   mvn spring-boot:run

Using the Packaged JAR
Build the project first:

mvn clean package
Then run the generated JAR:
java -jar target/nexusbank-0.0.1-SNAPSHOT.jar

Executing Unit and Integration Tests
Run All Tests
Execute the following command to run both unit and integration tests:

mvn test

Integration Tests with TestContainers
Make sure Docker is running on your machine, as TestContainers will start real containerized databases for integration testing.

API End-to-End Tests
Use tools like Postman or cURL to test the REST API endpoints and validate the responses. Automated API tests are available to simulate end-to-end workflows.

Deployment and Configuration
Docker Setup
Dockerfile: Contains the instructions to build the application’s container image.
docker-compose.yml: Configures the application along with its dependencies (e.g., databases) in a local development environment.
Build the Docker Image:


docker build -t nexusbank .
Run the Application with Docker Compose:

docker-compose up
Kubernetes Deployment
The project includes Kubernetes configuration files to deploy the application on a Kubernetes cluster.

deployment.yaml: Specifies the deployment details including replica count, container image, and environment variables.
service.yaml: Exposes the application within the Kubernetes cluster.
ingress.yaml: Configures external access to the application via an Ingress controller.
Deploy to Kubernetes:

kubectl apply -f kubernetes/deployment.yaml
kubectl apply -f kubernetes/service.yaml
kubectl apply -f kubernetes/ingress.yaml
Best Practices & Considerations
Hexagonal Architecture: By isolating the core business logic from technical details, the application remains flexible, easier to test, and simpler to maintain.
Test-Driven Development (TDD): Writing tests before implementation ensures that every component meets its specifications and facilitates rapid feedback during development.
Continuous Integration/Continuous Deployment (CI/CD): Utilize commit conventions (e.g., feat, fix, chore) to maintain a clear history and integrate with automated pipelines.
Documentation and Security: Keep API documentation (via OpenAPI) and security configurations updated. Regularly review and improve the documentation to assist all team members.
Smart Contract Integration: Ensure that any modifications to the smart contract are followed by recompilation using Truffle, and update the related contract artifacts accordingly.
