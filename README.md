🏭 Plant Monitoring System (Spring Boot + MQTT)

🚀 Overview

This project is a real-time plant/factory monitoring system that collects machine data using MQTT, processes it in a Spring Boot backend, and provides APIs for both live and historical data.

It is designed for department-based access control, where users can only view data related to their assigned department.

---

🧩 Features

🔴 Real-Time Data (IoT)

- MQTT-based data ingestion
- Live machine data stored in memory ("ConcurrentHashMap")
- Low-latency APIs for dashboards

🟢 Backend (Spring Boot)

- REST APIs for:
  - Live machine data
  - Department aggregated data
  - Historical machine data
- Clean layered architecture (Controller → Service → Repository)

🔐 Security

- Spring Security authentication
- Department-based authorization
- Custom "UserDetails" with department support
- "@AuthenticationPrincipal" for clean controller logic

🗄️ Database

- Stores historical machine and department data
- Optimized queries for latest data retrieval

---

🏗️ Tech Stack

- Backend: Spring Boot
- Security: Spring Security
- Messaging: MQTT
- Database: MySQL
- Build Tool: Maven
- Language: Java

---

📡 API Endpoints

🔹 Get Live Machine Data (Department-wise)

GET /department/getLiveData

🔹 Get Department Aggregated Data

GET /department/departmentData

🔹 Get Historical Machine Data

GET /department/machineData

---

⚙️ How It Works

1. Machines publish data via MQTT
2. Backend subscribes using "MqttSubscriber"
3. Data is stored in:
   - In-memory cache ("ConcurrentHashMap") → for live data
   - Database → for historical data
4. APIs filter data based on logged-in user’s department

---

🧠 Key Design Decisions

- ✅ Used "ConcurrentHashMap" for thread-safe real-time data storage
- ✅ Implemented department-based filtering in backend
- ✅ Avoided repeated DB calls using in-memory cache
- ✅ Used "@AuthenticationPrincipal" to simplify authentication logic
- ✅ Optimized queries for latest data retrieval

---

📦 Project Structure

controller/        → REST APIs
service/           → Business logic
repository/        → Database access
entity/            → JPA entities
DataCache/         → MQTT + live data storage
MQTTResponse/      → Incoming machine data model
security/          → Authentication & authorization

---

▶️ Running the Project

1️⃣ Clone the repo

git clone https://github.com/dineshbelhekar/IM-PT

2️⃣ Configure application.properties

spring.datasource.url=YOUR_DB_URL
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

3️⃣ Run the application

mvn spring-boot:run

---

📈 Future Improvements

- Add frontend dashboard (React / Angular)
- Implement WebSocket for real-time push updates
- Add caching layer (Redis)
- Deploy on cloud (AWS / Docker)
- Add alert system for machine anomalies

---

👨‍💻 Author

- Dinesh Belhekar

---

