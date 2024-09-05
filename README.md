## Project Overview

This project is a simple banking application implementing CQRS (Command Query Responsibility Segregation) and Event Sourcing patterns. It's built using Spring Boot for the backend, MongoDB and PostgreSQL for data storage, Kafka as the message broker for microservices communication. This architecture ensures scalability, data consistency, and high availability.

## Tech Stack

- Java 17
- Spring Boot
- Spring Cloud Stream
- MongoDB
- PostgreSQL
- Kafka
- Docker

## Prerequisites

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Build Docker Image
Start the application with Docker Compose
```sh
docker-compose up -d 
```

## Usage

This project exposes the following API endpoints:

- **GET /api/v1/account/{id}** - Retrieves account details by ID
- **POST /api/v1/open-account** - Creates a new account
- **POST /api/v1/deposit-funds/{accountsId}** - Deposits funds into the specified account
- **POST /api/v1/withdraw-funds/{accountsId}** - Withdraws funds from the specified account

## License

[MIT](https://choosealicense.com/licenses/mit/)
