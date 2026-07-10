# 🏭 Real-Time MQTT-Based Factory Monitoring System

A backend system that ingests live machine telemetry from factory-floor devices over **MQTT**, processes it in real time, and exposes it to role-specific dashboards through a **secure, role-based REST + WebSocket API**. Built with **Spring Boot**, **MySQL**, and **Redis**, it also automates maintenance alerting and daily power-usage reporting.

> Originally scaffolded as a simple plant-monitoring API, the project has since grown into a multi-role industrial IoT backend with JWT authentication, live WebSocket push updates, Redis-backed shared state, and an automated machine-failure/maintenance workflow.

---

## 🚀 Overview

Factories run many machines across departments, and floor managers, department managers, operators, and maintenance technicians all need different views of the same data — live readings, historical trends, and failure alerts — without stepping on each other's data.

This system:
- Subscribes to an MQTT broker and ingests per-machine sensor data (voltage, current, unit production, health condition) in real time.
- Aggregates readings in memory and periodically persists machine, department, and plant-level summaries to MySQL.
- Pushes live data to connected clients over WebSocket/STOMP, filtered to each user's department and assigned machine range.
- Detects machine failures automatically from the incoming stream, raises maintenance alerts to available technicians, and tracks alert lifecycle and downtime.
- Sends automated daily email reports on power consumption to admins and department managers.
- Enforces all of the above through JWT-based authentication and role-based authorization (Admin, Plant Manager, Department Manager, Operator, Technician).

---

## 🧩 Key Features

### 🔴 Real-Time Data Ingestion (MQTT)
- Subscribes to topic pattern `plant/{department}/{machineId}/data` on a public HiveMQ broker using Eclipse Paho.
- Deserializes incoming JSON payloads (voltage, current, unit production, machine condition) into typed objects.
- Maintains a thread-safe, in-memory live-data cache (`ConcurrentHashMap`) for low-latency reads — no DB round-trip needed for "current state" queries.

### 📊 Data Aggregation & Persistence
- A rolling in-memory aggregator computes average power (voltage × current) and latest production count per machine as messages arrive.
- A scheduled job (every 30 minutes) flushes aggregated machine-, department-, and plant-level metrics into MySQL for historical/trend queries, then resets the in-memory window.

### 📡 Live Push Updates (WebSocket / STOMP)
- A STOMP-over-WebSocket endpoint (`/ws`) pushes live machine data to each connected user every 2 seconds.
- Updates are scoped server-side: a manager with plant-wide access (department `"A"`) receives everything, while other users only receive data for their department and their assigned machine ID range.
- WebSocket connections are authenticated via a custom STOMP `CONNECT`-frame interceptor that validates a JWT before allowing a session.

### 🛠️ Automated Maintenance Alerting
- When a machine reports an unhealthy `condition` flag, the system automatically raises a maintenance alert (once per failure — no duplicate alerts) and pushes a notification to every currently-available maintenance technician over WebSocket.
- Technicians can accept an alert (assigning themselves) and mark it complete via REST endpoints; alert status (`PENDING → STARTED → DONE`) and downtime duration are persisted for reporting.
- Technician availability and machine alert state are tracked in **Redis** so state is shared and consistent across the running instance(s).

### 🔐 Authentication & Role-Based Access Control
- Stateless authentication via JWT (issued on login, validated per-request through a custom filter).
- Passwords hashed with BCrypt.
- Fine-grained authorization rules per endpoint group:

  | Role | Access |
  |---|---|
  | `ADMIN` | Employee management (`/admin/employee/**`) |
  | `PLANTMANAGER` | Full plant visibility (`/plant/**`), plus department & machine data |
  | `DEPTMANAGER` | Department-scoped data (`/department/**`) |
  | `OPERATOR` | Live machine data for their own department (`/machine/**`) |
  | `Maintenance` (Technician) | Maintenance alert queue and accept/complete actions |

### 📧 Automated Reporting
- A daily cron job (midnight) computes each department's and the plant's total power consumption over the last 24 hours and emails a summary report to Admins and Department Managers via Spring Mail.

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 (Web, Security, Data JPA, WebSocket, Mail, Scheduling) |
| Messaging (IoT ingestion) | MQTT (Eclipse Paho client, HiveMQ public broker) |
| Real-time push | WebSocket + STOMP (SockJS fallback) |
| Auth | JWT (jjwt), Spring Security, BCrypt |
| Database | MySQL (JPA/Hibernate) |
| Cache / shared state | Redis (Spring Data Redis) |
| Email | Spring Boot Starter Mail (SMTP) |
| Build tool | Maven |
| Boilerplate reduction | Lombok |

---

## 🏛️ Architecture

```
                ┌────────────────────┐
 Factory        │   MQTT Broker      │
 Machines  ───▶ │ (plant/dept/id/data)│
                └─────────┬──────────┘
                          │ subscribe
                          ▼
                ┌────────────────────┐
                │   MqttSubscriber   │──▶ Live cache (ConcurrentHashMap)
                │  (Spring Component)│──▶ AggregationCache (avg power/production)
                └─────────┬──────────┘──▶ MaintenanceAlertService (failure detection)
                          │
        ┌─────────────────┼───────────────────────┐
        ▼                 ▼                       ▼
 Scheduled (30 min)   Scheduled (2 sec)     On failure / recovery
 flush to MySQL       push via WebSocket    raise/clear alert (Redis + MySQL)
 (Machine/Dept/       to subscribed users    notify available technicians
  Plant tables)       (STOMP, dept/range     over WebSocket
                       filtered)
                          │
                          ▼
                ┌────────────────────┐
                │   REST API layer   │  ◀── JWT-authenticated,
                │ (role-based access)│      role-authorized clients
                └────────────────────┘
```

**Layered design:** `controller → service → repository → entity`, with cross-cutting concerns (MQTT ingestion, caching, scheduling, security) isolated into their own packages.

---

## 📡 API Reference

All endpoints (except `/user/**` and `/ws/**`) require a `Bearer <JWT>` Authorization header.

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/user/login` | Authenticates a user and returns a signed JWT |

### Admin (`ROLE_ADMIN`)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/admin/employee/add` | Create a new user/employee |
| GET | `/admin/employee/getall` | List all employees |
| GET | `/admin/employee/getbyid/{employeeId}` | Fetch a single employee |
| PUT | `/admin/employee/update` | Update employee details |
| DELETE | `/admin/employee/delete/{employeeId}` | Remove an employee |

### Plant Manager (`ROLE_PLANTMANAGER`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/plant/getallemployee` | All employees, plant-wide |
| GET | `/plant/getLiveData` | Live data for every machine in the plant |
| GET | `/plant/plantData` | Latest aggregated plant-level metrics |
| GET | `/plant/departmentData` | Aggregated metrics for every department |
| GET | `/plant/machineData` | Historical data for every machine |

### Department Manager (`ROLE_PLANTMANAGER` or `ROLE_DEPTMANAGER`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/department/getallemployee` | Employees in the caller's department |
| GET | `/department/getLiveData` | Live data, filtered to the caller's department |
| GET | `/department/departmentData` | Latest aggregated metrics for the department |
| GET | `/department/machineData` | Historical machine data for the department |

### Operator (`ROLE_PLANTMANAGER`, `ROLE_DEPTMANAGER`, or `ROLE_OPERATOR`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/machine/getLiveData` | Live data for machines in the caller's department |

### General (any authenticated user)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/role` / `/general/role` | Returns the caller's role/designation |
| POST | `/general/maintenance_alert/accept` | Technician accepts a raised maintenance alert |
| POST | `/general/maintenance_alert/completed` | Technician marks a maintenance job complete |

### WebSocket
| Endpoint | Description |
|---|---|
| `/ws` (SockJS/STOMP) | Live data channel (`/queue/messages`); requires a valid JWT in the STOMP `CONNECT` frame |

---

## ⚙️ How It Works — Data Flow

1. **Ingestion** — Machines (or simulators) publish JSON telemetry to `plant/{department}/{machineId}/data` on the MQTT broker.
2. **Processing** — `MqttSubscriber` parses each message, tags it with department/machine ID, updates the live cache, feeds the aggregation cache, and checks the machine's health condition.
3. **Failure handling** — If a machine reports an unhealthy condition, `MaintenanceAlertService` raises an alert (once), persists it, and notifies available technicians in real time. Recovery clears the alert and records downtime.
4. **Live delivery** — Every 2 seconds, `LiveDataScheduler` pushes each connected user a WebSocket update containing only the machines relevant to them.
5. **Persistence** — Every 30 minutes, `DataSavingScheduler` writes aggregated machine/department/plant snapshots to MySQL and resets the in-memory aggregation window.
6. **Reporting** — Once a day, `MailScheduler` emails 24-hour power-consumption summaries to Admins (plant-wide) and Department Managers (their department).
7. **Access control** — Every REST/WebSocket interaction is authenticated via JWT and authorized against the caller's role and department.

---

## 🧠 Key Design Decisions

- **In-memory cache for live reads, DB for history** — avoids hitting MySQL for high-frequency "current state" queries while still preserving durable historical data.
- **Redis for cross-cutting shared state** — machine alert status and technician availability live in Redis (not just JVM memory), so state stays consistent and is not lost on a single-instance restart.
- **Idempotent alerting** — failure detection checks existing alert state before raising a new one, preventing duplicate alerts from a stream of repeated "unhealthy" readings.
- **Server-side data scoping** — rather than trusting the client to filter, both REST responses and WebSocket pushes are filtered server-side by department/machine range based on the authenticated user, enforced independently of the UI.
- **Stateless auth everywhere, including WebSocket** — a custom STOMP channel interceptor validates the JWT on the `CONNECT` frame so the same auth model covers REST and real-time channels.
- **Scheduled aggregation windows** — averaging voltage/current over a rolling window (rather than storing every raw reading) keeps storage and query costs predictable at scale.

---

## 📦 Project Structure

```
src/main/java/com/example/IM/PT/
├── controller/        REST endpoints per role (Admin, PlantManager, DeptManager, Operator, Auth, General)
├── service/            Business logic (users, data access/storage, maintenance alerts, Redis, email, reports)
├── repository/         Spring Data JPA repositories
├── Entity/              JPA entities (User, MachineData, DepartmentData, PlantData, MaintenanceAlert)
├── DTO/                 Request/response and internal data transfer objects
├── DataCache/           In-memory + Redis-backed caches (live data, aggregation, connected users, machine state)
├── MQTTResponce/        MQTT payload model
├── aggregation/         Rolling average computation per machine
├── configuration/       MQTT, WebSocket, Redis, and Spring Security configuration
├── filter/              JWT request filter + WebSocket JWT channel interceptor
├── util/                JWT generation/validation utility
├── enums/               Maintenance and machine alert status enums
├── scheduler/           Scheduled jobs (data flush, live push, daily email report)
└── eventListener/       WebSocket connect/disconnect session tracking
```

---

## ▶️ Running the Project

**1. Clone the repository**
```bash
git clone https://github.com/dineshbelhekar/Real-Time_MQTT_Based_Factory_Monitoring_System.git
cd Real-Time_MQTT_Based_Factory_Monitoring_System
```

**2. Configure `src/main/resources/application.properties`**
```properties
spring.datasource.url=jdbc:mysql://<host>:3306/<db_name>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>

spring.data.redis.host=<redis-host>
spring.data.redis.port=<redis-port>
spring.data.redis.password=<redis-password>

spring.mail.host=<smtp-host>
spring.mail.port=<smtp-port>
spring.mail.username=<smtp-username>
spring.mail.password=<smtp-password>
```
> ⚠️ Do not commit real credentials. Use environment variables or a secrets manager in production, and externalize the JWT signing key rather than hardcoding it.

**3. Run the application**
```bash
./mvnw spring-boot:run
```

**4. Publish test data** to the broker `broker.hivemq.com:1883` on a topic like `plant/A/101/data` with a JSON payload, e.g.:
```json
{
  "condition": true,
  "current": 4.2,
  "voltage": 230.0,
  "unitProduction": 12
}
```

---

## 📈 Future Improvements

- Frontend dashboard (React/Angular) consuming the REST + WebSocket APIs
- Move the MQTT broker to a private/self-hosted instance (public HiveMQ broker is for testing only)
- Externalize the JWT secret and other credentials via environment variables / a secrets manager
- Containerize with Docker and add CI/CD
- Add refresh tokens and token revocation
- Add integration tests around the MQTT ingestion → alerting pipeline
- Machine-learning-based predictive maintenance instead of simple condition-flag alerting

---

## 👨‍💻 Author

**Dinesh Belhekar**
