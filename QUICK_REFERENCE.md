# QUICK REFERENCE GUIDE
## Smartoperation Application Commands & URLs

---

## 🚀 RUNNING THE APPLICATION

### Start All Services (Docker)
```powershell
cd "c:\GMU\Summer 2024\Projects\Smartoperation"
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f

# View specific service logs
docker logs -f smartoperation-backend-1
docker logs -f smartoperation-frontend-1
docker logs -f smartoperation-cronjob-1
docker logs -f smartoperation-mysql-1
```

### Check Status
```powershell
docker-compose ps
```

---

## 🌐 ACCESS POINTS

| Service | URL | Credentials |
|---------|-----|-------------|
| Frontend | http://localhost:5173 | ceo@smart.com / password |
| Backend API | http://localhost:8080/api | N/A (public) |
| MySQL | localhost:3307 | smartuser / smartpassword |
| WebSocket | ws://localhost:8080/ws-alerts | N/A (real-time) |

---

## 🔑 TEST CREDENTIALS

### Pre-loaded Users (Use to Login):
```
Email: ceo@smart.com          | Role: OWNER
Email: alice@smart.com        | Role: MANAGER
Email: bob@smart.com          | Role: MANAGER
Email: charlie@smart.com      | Role: MANAGER
Email: dan@smart.com          | Role: EMPLOYEE
Email: eve@smart.com          | Role: EMPLOYEE
Email: frank@smart.com        | Role: EMPLOYEE
Email: test@gmail.com         | Role: EMPLOYEE (newly added)

All passwords: "password"
```

---

## 📡 API ENDPOINTS

### Authentication
```
POST /api/auth/login
Body: { "email": "ceo@smart.com", "password": "password" }
Response: { "token": "jwt-token", "user": {...} }

POST /api/auth/signup
Body: { "email": "new@test.com", "name": "Name", "role": "EMPLOYEE", "password": "password" }
Response: { "token": "jwt-token", "user": {...} }
```

### Tasks
```
GET /api/tasks
Response: [ { "id": 1, "title": "...", "status": "...", ... }, ... ]

POST /api/tasks
Body: { "title": "New Task", "priority": "HIGH", "description": "..." }
Response: { "id": 6, "title": "New Task", ... }

PUT /api/tasks/{id}/status
Body: { "status": "IN_PROGRESS" }
Response: { "id": 1, "status": "IN_PROGRESS", ... }

GET /api/tasks/{id}/comments
Response: [ { "id": 1, "content": "...", ... }, ... ]

POST /api/tasks/{id}/comments
Body: { "content": "Comment text", "authorId": 1 }
Response: { "id": 1, "content": "Comment text", ... }
```

---

## 🗄️ DATABASE COMMANDS

### Connect to MySQL
```powershell
docker exec -it smartoperation-mysql-1 mysql -u smartuser -p"smartpassword" smartoperation
```

### Common Queries
```sql
-- View all users
SELECT * FROM app_user;

-- View all tasks
SELECT * FROM task;

-- View tasks by status
SELECT * FROM task WHERE status = 'OPEN';

-- View user's tasks
SELECT * FROM task WHERE assignee_id = 1;

-- Add test user
INSERT INTO app_user (email, name, password_hash, role) 
VALUES ('newuser@test.com', 'New User', 'password', 'EMPLOYEE');

-- Update user password
UPDATE app_user SET password_hash = 'newpassword' WHERE email = 'user@test.com';

-- View task risk analysis
SELECT id, title, ai_analysis FROM task;
```

---

## 🧪 TESTING

### Run Test Suite
```powershell
cd backend
./gradlew test

# Or Windows
gradlew.bat test

# Run specific test
gradlew test --tests SmartoperationIntegrationTests
```

### Run Backend
```powershell
cd backend
./gradlew bootRun

# Or Windows
gradlew.bat bootRun
```

### Run Frontend (Dev Mode)
```powershell
cd frontend
npm install
npm run dev
```

### Test API with cURL
```powershell
# Get all tasks
curl http://localhost:8080/api/tasks

# Login
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{"email":"ceo@smart.com","password":"password"}'

# Create task
curl -X POST http://localhost:8080/api/tasks `
  -H "Content-Type: application/json" `
  -d '{"title":"New Task","priority":"HIGH"}'
```

---

## 🔄 GITHUB WORKFLOW

### Initial Setup
```powershell
cd "c:\GMU\Summer 2024\Projects\Smartoperation"

# Initialize git
git init

# Configure user
git config user.name "Your Name"
git config user.email "your-email@gmail.com"

# Create .gitignore file (see GITHUB_SETUP_GUIDE.md)

# Add remote
git remote add origin https://github.com/YOUR-USERNAME/smartoperation.git

# Rename to main branch
git branch -M main
```

### Commit & Push
```powershell
# Check status
git status

# Add files
git add .

# Commit
git commit -m "Your commit message"

# Push
git push -u origin main

# Future pushes
git push
```

### Useful Git Commands
```powershell
# View commit history
git log --oneline

# See what changed
git diff

# Create new branch
git checkout -b feature/branch-name

# Switch branches
git checkout main

# Merge branch
git merge feature/branch-name

# Delete branch
git branch -d feature/branch-name
```

---

## 🚨 TROUBLESHOOTING

### Issue: "Cannot connect to MySQL"
```powershell
# Check if MySQL container is running
docker ps | Select-String mysql

# Check MySQL logs
docker logs smartoperation-mysql-1

# Restart MySQL
docker restart smartoperation-mysql-1
```

### Issue: "Frontend not loading"
```powershell
# Check frontend logs
docker logs smartoperation-frontend-1

# Restart frontend
docker restart smartoperation-frontend-1

# Check port 5173 is accessible
curl http://localhost:5173
```

### Issue: "Backend API timeout"
```powershell
# Check backend logs for errors
docker logs smartoperation-backend-1

# Restart backend
docker restart smartoperation-backend-1

# Check database connection
docker logs smartoperation-backend-1 | findstr /C:"HikariPool" /C:"Database"
```

### Issue: "Rate limiting blocking requests"
```powershell
# Rate limit is 50 requests/minute per IP
# Wait 1 minute for reset, or
# Modify limit in RateLimitInterceptor.java (change 50 to higher value)
```

### Issue: "Cronjob not running"
```powershell
# Check cronjob logs
docker logs smartoperation-cronjob-1 | tail -20

# Restart cronjob
docker restart smartoperation-cronjob-1
```

---

## 📊 MONITORING

### Check All Container Health
```powershell
docker ps --format "table {{.Names}}\t{{.Status}}"
```

### View Resource Usage
```powershell
docker stats

# Or specific container
docker stats smartoperation-backend-1
```

### Check Network
```powershell
# View all containers network
docker network ls

# Inspect smartoperation network
docker network inspect smartoperation_default
```

---

## 🔐 SECURITY NOTES

### JWT Token
- **Expires in**: 11 hours
- **Algorithm**: HMAC SHA256
- **Secret**: Configured in `application.properties`
- **How to change**: Update `jwt.secret` value

### Rate Limiting
- **Limit**: 50 requests/minute
- **Per**: IP address
- **Response**: HTTP 429 (Too Many Requests)
- **Reset**: After 1 minute automatically

### Database
- **User**: smartuser
- **Password**: smartpassword
- **Production Note**: Change these in production!
- **Connection**: Uses docker service name `mysql`

### CORS
- **Configured for**: All origins (*)
- **Production Note**: Restrict to specific domains
- **File**: `config/WebMvcConfig.java`

---

## 📝 IMPORTANT FILES

| File | Purpose |
|------|---------|
| `docker-compose.yml` | Container orchestration |
| `.github/workflows/main.yml` | CI/CD pipeline |
| `application.properties` | Backend configuration |
| `SYSTEM_VERIFICATION_REPORT.md` | Complete test results |
| `TESTING_REPORT.md` | Detailed component analysis |
| `GITHUB_SETUP_GUIDE.md` | GitHub setup instructions |

---

## ⚙️ SYSTEM INFORMATION

```
Backend:
  - Framework: Spring Boot 3.x
  - Language: Java 17
  - Port: 8080
  - Database: MySQL 8.0.46

Frontend:
  - Framework: React 18 + TypeScript
  - Build Tool: Vite
  - Port: 5173
  - Package Manager: npm

Cronjob:
  - Runtime: Node.js 18
  - Scheduler: node-cron
  - Frequency: Every 1 minute
  
Database:
  - DBMS: MySQL 8.0
  - Host: localhost:3307
  - Pool: HikariCP (10 connections)
  - Schema: smartoperation

CI/CD:
  - Platform: GitHub Actions
  - Trigger: Push to main
  - Build Tools: Gradle, npm
```

---

## 📞 HELP & SUPPORT

### Documentation
- **[SYSTEM_VERIFICATION_REPORT.md](SYSTEM_VERIFICATION_REPORT.md)** - Complete system status
- **[TESTING_REPORT.md](TESTING_REPORT.md)** - Detailed test results
- **[GITHUB_SETUP_GUIDE.md](GITHUB_SETUP_GUIDE.md)** - GitHub integration steps
- **[SmartoperationIntegrationTests.java](backend/src/test/java/com/smartoperation/SmartoperationIntegrationTests.java)** - Test examples

### Common Issues
1. **Services won't start**: Check Docker daemon is running
2. **Port already in use**: Change ports in docker-compose.yml
3. **Database errors**: Verify MySQL credentials in application.properties
4. **Frontend blank**: Clear browser cache, hard refresh (Ctrl+Shift+R)
5. **API timeouts**: Check backend logs for connection issues

---

**Last Updated**: April 25, 2026  
**Status**: ✅ All Systems Operational  
**Next Step**: Follow GITHUB_SETUP_GUIDE.md to push code
