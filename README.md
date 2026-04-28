# Smartoperation - Task Management System with AI Risk Analysis

![Status](https://img.shields.io/badge/Status-Production%20Ready-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)
![Java](https://img.shields.io/badge/Java-17%2B-orange)
![React](https://img.shields.io/badge/React-18-61DAFB?logo=react)

A comprehensive **task management platform** with **AI-powered risk analysis**, real-time WebSocket alerts, JWT authentication, and rate limiting. Built with Spring Boot backend, React frontend, and MySQL database, fully containerized with Docker.

---

## 🎯 Project Overview

Smartoperation is an enterprise-grade task management system designed for teams to:
- **Create and manage** tasks across departments
- **Assign tasks** to team members with priority levels
- **Track task status** in real-time with WebSocket notifications
- **Analyze risk** using AI algorithms based on due dates and priorities
- **Manage departments** and team hierarchies
- **Secure access** with JWT authentication and role-based controls
- **Monitor performance** through comprehensive dashboards

---

## ✨ Key Features

### Backend Features
- ✅ **REST API** with Spring Boot 3.x (Java 17)
- ✅ **JWT Authentication** with 11-hour expiration
- ✅ **Rate Limiting** (50 requests/minute per IP using Bucket4j)
- ✅ **WebSocket Support** with STOMP for real-time alerts
- ✅ **CORS Configuration** for cross-origin requests
- ✅ **Database Integration** with Hibernate ORM and Spring Data JPA
- ✅ **Comprehensive Logging** for debugging

### Frontend Features
- ✅ **React 18** with TypeScript
- ✅ **Responsive Dashboard** with KPI cards
- ✅ **Real-time Notifications** via WebSocket
- ✅ **Task Management UI** with Create/Read/Update operations
- ✅ **Role Simulation** (Owner/Manager/Employee)
- ✅ **Vite** build tool for fast development

### Cronjob Features
- ✅ **AI Risk Analysis** running every minute
- ✅ **Automatic Risk Scoring** based on task properties
- ✅ **Database Updates** with risk assessments
- ✅ **Node.js 18** with node-cron scheduler
- ✅ **Zod Schema Validation** for data integrity

### DevOps & Infrastructure
- ✅ **Docker Compose** orchestrating 4 microservices
- ✅ **GitHub Actions** CI/CD pipeline
- ✅ **Automated Builds** for Java and Node.js
- ✅ **MySQL 8.0** containerized database
- ✅ **Health Checks** for all services

---

## 🏗️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Backend** | Spring Boot | 3.x |
| **Language** | Java | 17+ |
| **Frontend** | React + TypeScript | 18 |
| **Build Tool** | Vite | Latest |
| **Database** | MySQL | 8.0.46 |
| **Containerization** | Docker & Docker Compose | 29.4.0+ |
| **Cronjob** | Node.js | 18 |
| **Authentication** | JWT (HMAC SHA256) | Custom |
| **Real-time** | WebSocket (STOMP) | Spring STOMP |
| **CI/CD** | GitHub Actions | Standard |

---

## 📁 Project Structure

```
smartoperation/
├── backend/                          # Spring Boot application
│   ├── src/main/java/com/smartoperation/
│   │   ├── BackendApplication.java
│   │   ├── DummyDataConfig.java      # Sample data initialization
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── WebMvcConfig.java
│   │   │   ├── WebSocketConfig.java
│   │   │   └── RateLimitInterceptor.java
│   │   ├── controller/
│   │   │   ├── AuthController.java   # /api/auth endpoints
│   │   │   └── TaskController.java   # /api/tasks endpoints
│   │   ├── dto/
│   │   │   ├── LoginRequest.java
│   │   │   └── SignupRequest.java
│   │   ├── model/
│   │   │   ├── AppUser.java
│   │   │   ├── Task.java
│   │   │   ├── Department.java
│   │   │   └── TaskComment.java
│   │   ├── repository/              # Spring Data JPA repositories
│   │   ├── security/
│   │   │   ├── JwtUtil.java
│   │   │   └── JwtAuthenticationFilter.java
│   ├── src/test/java/               # Integration tests
│   ├── Dockerfile
│   ├── build.gradle
│   └── pom.xml
│
├── frontend/                         # React application
│   ├── src/
│   │   ├── components/
│   │   │   ├── Auth.tsx              # Login/Signup components
│   │   │   └── SharedComponents.tsx
│   │   ├── App.tsx                   # Main dashboard
│   │   ├── main.tsx
│   │   └── index.css
│   ├── Dockerfile
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── package.json
│   └── index.html
│
├── cronjob/                          # Node.js cronjob
│   ├── index.js                      # AI Risk Analysis scheduler
│   ├── Dockerfile
│   └── package.json
│
├── docker-compose.yml                # Orchestration file
├── .github/workflows/main.yml         # CI/CD pipeline
├── GITHUB_SETUP_GUIDE.md             # Git setup instructions
├── TESTING_REPORT.md                 # Test suite documentation
├── SYSTEM_VERIFICATION_REPORT.md     # Verification results
└── README.md                         # This file
```

---

## 🚀 Quick Start

### Prerequisites
- **Docker** 29.4.0+ ([Install](https://www.docker.com/products/docker-desktop))
- **Docker Compose** 2.20.0+ (included with Docker Desktop)
- **Git** for version control
- **Java 17+** (optional, for local development)
- **Node.js 18+** (optional, for local cronjob)

### Installation & Setup

#### 1. Clone or Download Project
```bash
# If cloning from GitHub
git clone https://github.com/YOUR-USERNAME/smartoperation.git
cd smartoperation

# Or navigate if already downloaded
cd "c:\GMU\Summer 2024\Projects\Smartoperation"
```

#### 2. Start All Services with Docker Compose
```powershell
# Start all containers in background
docker-compose up -d

# Verify services are running
docker-compose ps
```

**Expected output:**
```
NAME                        STATUS              PORTS
smartoperation-mysql-1      Up (healthy)        0.0.0.0:3307->3306/tcp
smartoperation-backend-1    Up                  0.0.0.0:8080->8080/tcp
smartoperation-frontend-1   Up                  0.0.0.0:5173->5173/tcp
smartoperation-cronjob-1    Up                  (no ports)
```

#### 3. Access the Application
| Component | URL | Purpose |
|-----------|-----|---------|
| **Frontend** | http://localhost:5173 | Dashboard & UI |
| **Backend API** | http://localhost:8080/api | REST API |
| **Database** | localhost:3307 | MySQL (internal) |

#### 4. Login Credentials

**Pre-configured users:**
```
Email:    ceo@smart.com
Password: password
Role:     OWNER

Email:    test@gmail.com
Password: password
Role:     EMPLOYEE
```

---

## 📡 API Endpoints

### Authentication
```
POST   /api/auth/login      - User login (returns JWT token)
POST   /api/auth/signup     - User registration
```

### Tasks
```
GET    /api/tasks           - Get all tasks (paginated)
POST   /api/tasks           - Create new task
PUT    /api/tasks/{id}/status - Update task status
GET    /api/tasks/{id}      - Get task details
```

### Comments
```
GET    /api/tasks/{id}/comments - Get task comments
POST   /api/tasks/{id}/comments - Add comment to task
```

### WebSocket
```
WS     /ws-alerts           - Real-time alert channel
Topic: /topic/alerts        - Subscribe to notifications
```

---

## 🧪 Testing

### Run Integration Tests
```powershell
cd backend

# Using Gradle
gradlew test

# View test results
# Results appear in: backend/build/reports/tests/test/index.html
```

**Test Coverage:**
- ✅ 16 comprehensive integration tests
- ✅ Authentication & JWT validation
- ✅ API endpoints (CRUD operations)
- ✅ Rate limiting enforcement
- ✅ Database persistence
- ✅ WebSocket connectivity
- ✅ Security headers & CORS

For detailed test information, see [TESTING_REPORT.md](TESTING_REPORT.md)

---

## 🔧 Development

### Local Backend Development
```powershell
cd backend

# Build project
gradlew build

# Run Spring Boot application
gradlew bootRun
# Runs on http://localhost:8080
```

### Local Frontend Development
```powershell
cd frontend

# Install dependencies
npm install

# Start development server with HMR
npm run dev
# Runs on http://localhost:5173
```

### Local Cronjob Development
```powershell
cd cronjob

# Install dependencies
npm install

# Run cronjob
node index.js
```

---

## 📊 Database Schema

### Tables

**app_user**
```sql
- id (INT, Primary Key)
- email (VARCHAR, UNIQUE)
- name (VARCHAR)
- password_hash (VARCHAR)
- role (ENUM: OWNER, MANAGER, EMPLOYEE)
- department_id (FOREIGN KEY)
- manager_id (FOREIGN KEY)
```

**task**
```sql
- id (INT, Primary Key)
- title (VARCHAR)
- description (TEXT)
- status (ENUM: OPEN, IN_PROGRESS, COMPLETED)
- priority (ENUM: LOW, MEDIUM, HIGH)
- due_date (DATETIME)
- assignee_id (FOREIGN KEY)
- department_id (FOREIGN KEY)
- ai_analysis (VARCHAR) - AI risk score
- created_at (TIMESTAMP)
```

**department**
```sql
- id (INT, Primary Key)
- name (VARCHAR, UNIQUE)
```

**task_comment**
```sql
- id (INT, Primary Key)
- content (TEXT)
- author_id (FOREIGN KEY)
- task_id (FOREIGN KEY)
- created_at (TIMESTAMP)
```

---

## 🔐 Security Features

### Authentication & Authorization
- **JWT Tokens** with 11-hour expiration
- **HMAC SHA256** signing algorithm
- **Role-Based Access Control** (OWNER/MANAGER/EMPLOYEE)
- **Password Hashing** with Spring Security

### API Security
- **CORS Configuration** allowing authorized origins
- **Rate Limiting** (50 requests/minute per IP)
- **HTTP Security Headers** (Content-Type, X-Frame-Options, etc.)
- **SQL Injection Prevention** via prepared statements

### Data Protection
- **MySQL Connection** with useSSL flag
- **HikariCP Connection Pool** (10 connections)
- **Database Credentials** in environment variables

---

## 📈 Performance & Monitoring

### Database
- **Connection Pool**: HikariCP (10 max connections)
- **Query Optimization**: Indexed primary keys
- **Auto-Schema Generation**: Hibernate ddl-auto=update

### Backend
- **Async Processing**: WebSocket for real-time updates
- **Caching**: Spring Cache (if enabled)
- **Monitoring**: Comprehensive logging

### Frontend
- **Build Optimization**: Vite tree-shaking
- **Code Splitting**: Lazy loading components
- **WebSocket Optimization**: Efficient message handling

### Cronjob
- **Scheduled Tasks**: Every 1 minute
- **Efficient Queries**: Batch processing
- **Database Pooling**: mysql2/promise

---

## 🐳 Docker Commands

```powershell
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f cronjob

# Restart a service
docker-compose restart backend

# Remove all containers and volumes
docker-compose down -v

# Rebuild images
docker-compose build
docker-compose build --no-cache
```

---

## 🚀 Deployment

### Push to GitHub
Follow the [GITHUB_SETUP_GUIDE.md](GITHUB_SETUP_GUIDE.md) for:
1. Initialize git repository
2. Create GitHub repository
3. Push code to main branch
4. GitHub Actions CI/CD automatically runs

### CI/CD Pipeline
The `.github/workflows/main.yml` workflow:
- ✅ Checks out code
- ✅ Sets up JDK 17
- ✅ Builds backend with Gradle
- ✅ Sets up Node.js 18
- ✅ Builds frontend with npm
- ✅ Runs on every push to main

---

## 📚 Documentation

| Document | Purpose |
|----------|---------|
| [GITHUB_SETUP_GUIDE.md](GITHUB_SETUP_GUIDE.md) | Complete Git & GitHub setup instructions |
| [TESTING_REPORT.md](TESTING_REPORT.md) | Detailed test suite documentation |
| [SYSTEM_VERIFICATION_REPORT.md](SYSTEM_VERIFICATION_REPORT.md) | System component verification results |
| [QUICK_REFERENCE.md](QUICK_REFERENCE.md) | Commands, credentials, endpoints |

---

## 🤝 Contributing

1. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make changes and test**
   ```powershell
   cd backend && gradlew test
   cd ../frontend && npm run build
   ```

3. **Commit with descriptive message**
   ```bash
   git commit -m "feat: Add feature description

   - Detailed change 1
   - Detailed change 2
   - Related issue #123"
   ```

4. **Push and create pull request**
   ```bash
   git push origin feature/your-feature-name
   ```

---

## 🐛 Troubleshooting

### Backend not starting
```powershell
# Check logs
docker-compose logs backend --tail 50

# Restart backend
docker-compose restart backend

# Verify MySQL is healthy
docker-compose ps mysql
```

### Frontend not loading
```powershell
# Check frontend logs
docker-compose logs frontend --tail 50

# Test frontend port
curl http://localhost:5173

# Restart frontend
docker-compose restart frontend
```

### Database connection issues
```powershell
# Check MySQL status
docker-compose logs mysql --tail 20

# Verify database exists
docker-compose exec mysql mysql -u smartuser -p smartoperation -e "SHOW TABLES;"

# Password when prompted: smartpassword
```

### Git permission denied
```powershell
# Use HTTPS instead of SSH
git remote set-url origin https://github.com/YOUR-USERNAME/smartoperation.git

# Use GitHub Personal Access Token
# Generate at: https://github.com/settings/tokens
```

---

## 📞 Support & Contact

For issues, questions, or suggestions:
- Create an **Issue** in GitHub
- Check [TROUBLESHOOTING.md](QUICK_REFERENCE.md#troubleshooting) section
- Review logs: `docker-compose logs`

---

## 📝 License

This project is licensed under the **MIT License** - see LICENSE file for details.

---

## ✅ Verification Checklist

After installation, verify:

- [ ] Docker services are running: `docker-compose ps`
- [ ] Frontend loads: `http://localhost:5173`
- [ ] Backend responds: `http://localhost:8080/api/tasks`
- [ ] Login works: Use credentials above
- [ ] Dashboard displays 5 tasks
- [ ] WebSocket connected (console shows no errors)
- [ ] Create a task and verify in database
- [ ] Tests pass: `cd backend && gradlew test`

---

## 🎉 You're All Set!

Your Smartoperation application is ready to use. Start managing tasks with AI-powered risk analysis today!

**Next Steps:**
1. Login to dashboard at http://localhost:5173
2. Create and manage tasks
3. Monitor AI risk analysis
4. Push code to GitHub following [GITHUB_SETUP_GUIDE.md](GITHUB_SETUP_GUIDE.md)
5. Run tests to ensure everything works

---

**Last Updated**: April 27, 2026  
**Version**: 1.0.0  
**Status**: Production Ready ✅
