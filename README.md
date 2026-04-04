# Event-Driven Order System

A microservices-based order management system built with **Java Spring Boot** and **Apache Kafka**, demonstrating asynchronous event-driven communication between independent services.

---

## Architecture

```
Client / API Caller
        │
        ├──────────────────────────────┐
        ▼                              ▼
 user-service (:8081)      order-service (:8083)       product-service (:8082)
  User management            Place orders                 Product catalog
        │                        │                              │
        │                        │ publishes OrderEvent         │
        │                        ▼                              │
        │              ┌─────────────────────┐                  │
        │              │  Apache Kafka 3.7   │                  │
        │              │  Topic: order-events│                  │
        │              └─────────┬───────────┘                  │
        │                        │ consumes                     │
        │                        ▼                              │
        │             notification-service (:8085)              │
        │               Sends order alerts                      │
        │                        │                              │
        └────────────────────────┼──────────────────────────────┘
                                 ▼
                       MySQL 8.0 (oms_db)
```

### Services

| Service               | Port | Role                                             |
|-----------------------|------|--------------------------------------------------|
| `user-service`        | 8081 | User registration and management                 |
| `product-service`     | 8082 | Product catalog (CRUD)                           |
| `order-service`       | 8083 | Order placement; publishes `OrderEvent` to Kafka |
| `notification-service`| 8085 | Consumes `OrderEvent`; sends notifications       |

### Infrastructure

| Component        | Version      | Notes                                        |
|------------------|--------------|----------------------------------------------|
| Apache Kafka     | 3.7.0        | KRaft mode (no Zookeeper required)           |
| MySQL            | 8.0          | Shared DB (`oms_db`) — see Known Limitations |

### Event Flow

1. A client calls `POST /orders` on **order-service**
2. order-service persists the order to MySQL and publishes an `OrderEvent` to the `order-events` Kafka topic
3. **notification-service** consumes the event asynchronously and dispatches a notification

---

## Getting Started

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) and Docker Compose
- Java 17+ (for local development without Docker)

### Run with Docker Compose

```bash
# Clone the repository
git clone https://github.com/Prasadkhose09/event-driven-order-system.git
cd event-driven-order-system

# Copy environment config and set your own values
cp .env.example .env

# Start all services
docker-compose up --build
```

Services will be available at:
- user-service → http://localhost:8081
- product-service → http://localhost:8082
- order-service → http://localhost:8083
- notification-service → http://localhost:8085

### Environment Variables

Copy `.env.example` to `.env` and update the values before running:

```env
MYSQL_ROOT_PASSWORD=your_secure_password
MYSQL_DATABASE=oms_db
```

> **Never commit `.env` to version control.** It is already listed in `.gitignore`.

---

## API Reference

### Order Service (`localhost:8083`)

| Method | Endpoint       | Description         |
|--------|----------------|---------------------|
| POST   | `/orders`      | Place a new order   |
| GET    | `/orders/{id}` | Get order by ID     |

### Product Service (`localhost:8082`)

| Method | Endpoint          | Description            |
|--------|-------------------|------------------------|
| GET    | `/products`       | List all products      |
| POST   | `/products`       | Add a new product      |
| GET    | `/products/{id}`  | Get product by ID      |

### User Service (`localhost:8081`)

| Method | Endpoint       | Description          |
|--------|----------------|----------------------|
| POST   | `/users`       | Register a new user  |
| GET    | `/users/{id}`  | Get user by ID       |

---

## Kafka Topics

| Topic           | Producer       | Consumer(s)           | Payload        |
|-----------------|----------------|-----------------------|----------------|
| `order-events`  | order-service  | notification-service  | `OrderEvent`   |

---

## Known Limitations & Planned Improvements

- **Shared database** — all services currently share a single `oms_db`. Each service should own its own schema/database to maintain loose coupling.
- **No healthchecks** — `depends_on` in Docker Compose does not wait for Kafka/MySQL to be ready. Services may fail on cold start. Healthchecks will be added.
- **No API Gateway** — there is no central ingress point. A Spring Cloud Gateway will be added.
- **No Outbox pattern** — if Kafka publish fails after a successful DB write, the event is lost. The Outbox pattern will be implemented for reliability.

---

## Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Kafka**
- **Spring Data JPA**
- **Apache Kafka 3.7 (KRaft)**
- **MySQL 8.0**
- **Docker / Docker Compose**

---

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.

---

## Author

**Prasad Khose** — [github.com/Prasadkhose09](https://github.com/Prasadkhose09)
