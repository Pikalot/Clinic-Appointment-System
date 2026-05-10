# CarePoint – Clinic Appointment System

A Spring Boot enterprise-style web application for online appointment scheduling. Built for CMPE 172 – Enterprise Software at San Jose State University.

---

## Tech Stack

- **Backend**: Java 17+, Spring Boot
- **Database**: MySQL (accessed via JDBC — no ORM)
- **Frontend**: Thymeleaf, HTML/CSS/JavaScript
- **Build Tool**: Maven

---

## Prerequisites

Make sure the following are installed before running the application:

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 17 or higher | https://adoptium.net |
| Maven | 3.8+ | https://maven.apache.org |
| MySQL | 8.0+ | https://dev.mysql.com/downloads |

---

## Environment Setup

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/clinic-appointment-system.git
cd clinic-appointment-system
```

### 2. Set Up the Database

Open MySQL and create the database:

```sql
source public/createdb.sql;
```

This will create the database, all tables, and seed initial data.

### 3. Configure Application Properties

Open `src/main/resources/application.properties` and update with your credentials:

```properties
spring.application.name=starter-demo
spring.datasource.url=jdbc:mysql://localhost:3306/Appointments
spring.datasource.username=[your-username]
spring.datasource.password=[your_password]
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Logging
logging.file.name=logs/app.log
```

### 4. Install Dependencies

Maven will automatically download all dependencies on first build. Run:

```bash
mvn clean install
```

### 5. Run the Application
 
**Option A — IntelliJ IDEA (recommended):**
 
Open the project in IntelliJ, navigate to `src/main/java/termproject/cas/Application.java` and click the green **Run** button.
 
**Option B — Maven CLI:**
 
```bash
mvn spring-boot:run
```
 
**Option C — JAR:**
 
```bash
java -jar target/starter-demo-0.0.1-SNAPSHOT.jar
```
 
The application will start at **http://localhost:8080**
 
---

## Mock Users Authentications

### Providers
| Providers | Auth     | 
|-----------|----------|
| provider1 | provider |
| provider2 | provider |
| provider3 | provider |
| provider4 | provider |

### Patients

| Patients  | Auth     | 
|-----------|----------|
| patient1  | patient  |
| patient2  | patient  |
| patient3  | patient  |
| patient4  | patient  |

---

## Key Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Home page |
| GET | `/bookappointment` | View available slots and appointments |
| GET | `/appointment/{id}` | View appointment detail |
| GET | `/doctors` | View all providers (developing) |
| GET | `/health` | System health check |
| POST | `/auth/login/patient` | Patient login |
| POST | `/auth/login/staff` | Staff login |
| GET | `/slots` | Get all available slots |
| POST | `/slots` | Create a new slot |
| PUT | `/slots/{id}/cancel` | Cancel a slot |
| GET | `/appointments` | Get all appointments |
| POST | `/appointments` | Book an appointment |
| PUT | `/appointments/{id}/cancel` | Cancel an appointment |
| GET | `/services` | Get all services |
| GET | `/providers` | Get all providers |
| GET | `/clinics` | Get all clinics |
| POST | `/notifications` | Mock notification service |

---

## Project Structure

```
src/
├── main/
│   ├── java/termproject/cas/
│   │   ├── assembler/       # Data mappers (ResultSet → Model)
│   │   ├── controller/      # Spring MVC controllers
│   │   ├── model/           # Domain models and DTOs
│   │   ├── repository/      # JDBC repositories
│   │   └── service/         # Business logic
│   └── resources/
│       ├── static/
│       │   ├── css/         # Stylesheets
│       │   └── js/          # JavaScript files
│       ├── templates/       # Thymeleaf HTML templates
│       └── application.properties
```

---

## Features

- Browse available appointment slots by date, clinic, and doctor
- Book appointments with service selection
- Cancel appointments with slot availability restoration
- Provider management of availability slots
- Double-booking prevention via optimistic locking
- Mock external notification service via REST
- System health check endpoint
- Role-based access: Patient, Provider, Staff, Admin
- Logging to file via SLF4J

---

## Health Check

Visit `http://localhost:8080/health` to verify the system status:

```json
{
  "status": "UP",
  "service": "Clinic Appointment System",
  "database": "CONNECTED",
  "notification": "CONNECTED"
}
```

---

## Logging

Application logs are written to `logs/app.log`. Key events logged include:

- Booking requests and confirmations
- Cancellation requests
- Slot conflicts and concurrency errors
- Notification service calls and responses

---

## Notes

- This application uses raw JDBC — no ORM frameworks (Hibernate/JPA)
- Authentication is session-based using browser `sessionStorage`
- The notification service is mocked via a local Spring controller at `POST /notifications`
