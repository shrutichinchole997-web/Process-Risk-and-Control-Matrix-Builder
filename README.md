# Tool-68 — Process Risk and Control Matrix Builder

## Overview
Process Risk and Control Matrix (RCM) Builder is a robust, enterprise-grade Spring Boot application designed to manage, evaluate, and report on organizational process risks and controls. It features secure JWT authentication, multi-level caching with Redis, professional email notifications via Thymeleaf, and automated audit logging.

## Core Features
- **Security**: JWT-based authentication with Role-Based Access Control (RBAC).
- **Caching**: Redis integration for high-performance data retrieval.
- **Auditing**: Automatic tracking of all create/update actions in a dedicated audit log.
- **Reporting**: Export all Risk Control Matrix data to CSV format.
- **Notifications**: Scheduled daily reminders and event-driven email alerts using professional Thymeleaf templates.
- **API Documentation**: Interactive Swagger UI for API exploration.

## Architecture Diagram

```text
+-------------------+       +-------------------+       +-------------------+
|                   |       |                   |       |                   |
|   Postman / UI    +------>+ Spring Boot API   +------>+   PostgreSQL DB   |
|   (REST Endpoints)|       | (Port 8080)       |       |   (Port 5432)     |
|                   |       |                   |       |                   |
+-------------------+       +-------+-----------+       +-------------------+
                                    |
                                    v
                            +-------+-----------+       +-------------------+
                            |                   |       |                   |
                            |   Redis Cache     +------>+   Mailpit (SMTP)  |
                            |   (Port 6379)     |       |   (Port 1025)     |
                            |                   |       |                   |
                            +-------------------+       +-------------------+
```

## Prerequisites
- **Java 17** (Amazon Corretto or OpenJDK)
- **Maven 3.9+**
- **Docker & Docker Compose**
- **Eclipse IDE** (for local development)

## Database Configuration
The project is configured to use PostgreSQL with the following credentials:
- **Database Name**: `Tool-68`
- **Username**: `root`
- **Password**: `root`

## Setup & Run Instructions

### 1. Running with Docker (Recommended for Demo)
Execute the following command in the root directory:
```bash
docker-compose up --build
```
This will start:
- **PostgreSQL**: `localhost:5432`
- **Redis**: `localhost:6379`
- **Mailpit**: `localhost:8025` (Web UI)
- **Backend API**: `localhost:8080`

### 2. Running in Eclipse IDE
1. Open Eclipse IDE.
2. Select **File > Import... > Existing Maven Projects**.
3. Browse to the `backend` folder and click **Finish**.
4. Right-click on the project > **Run As > Spring Boot App**.
   - *Note: Ensure PostgreSQL and Redis are running locally or via Docker before starting the app in Eclipse.*

## Environment Variables (.env)
| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | Database Host | `localhost` |
| `DB_NAME` | Database Name | `Tool-68` |
| `DB_USER` | Database Username | `root` |
| `DB_PASS` | Database Password | `root` |
| `REDIS_HOST` | Redis Host | `localhost` |
| `MAIL_HOST` | SMTP Host | `localhost` |
| `JWT_SECRET` | JWT Secret Key | `verysecretkey...` |

## Key API Endpoints
- **Auth**: `POST /api/auth/register`, `POST /api/auth/login`
- **PRC Management**: `GET /api/prc/all`, `POST /api/prc/create`
- **Reports**: `GET /api/prc/export` (Admin only)
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

## Test Coverage
The service layer is covered by 10+ JUnit 5 tests with Mockito, ensuring 80%+ business logic coverage.
