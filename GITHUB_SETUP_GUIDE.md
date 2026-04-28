# GitHub Setup & Deployment Guide

## Complete Steps to Connect Your Project to GitHub and Push Code

---

## Prerequisites
- ✅ GitHub Account (https://github.com)
- ✅ Git installed on your Windows machine
- ✅ Your project files ready
- ✅ Terminal/PowerShell access

---

## Part 1: Initialize Git Repository Locally

### Step 1.1: Open PowerShell in your project directory

```powershell
cd "c:\GMU\Summer 2024\Projects\Smartoperation"
```

### Step 1.2: Initialize Git and set user configuration

```powershell
# Initialize git repository
git init

# Configure your name and email
git config user.name "Your Name"
git config user.email "your-email@gmail.com"

# Verify configuration
git config --list
```

### Step 1.3: Create .gitignore file

Save this as `.gitignore` in your project root:

```
# Node modules
frontend/node_modules/
node_modules/
dist/
.next/
.nuxt/

# Java/Gradle
backend/build/
backend/target/
*.class
*.jar
*.war
*.ear
.gradle/
gradle/

# Database
*.sql
*.sqlite
mysql_data/

# Environment variables
.env
.env.local
.env.*.local
*.properties.local

# IDE
.vscode/
.idea/
*.swp
*.swo
*.sublime-*
*.code-workspace

# OS
.DS_Store
Thumbs.db
desktop.ini

# Docker (optional, keep for CI/CD)
# .dockerignore

# Logs
logs/
*.log
npm-debug.log*
yarn-debug.log*
yarn-error.log*

# Temporary files
tmp/
temp/
*.bak
*.swp
*~
```

---

## Part 2: Create GitHub Repository

### Step 2.1: Go to GitHub and create new repository

1. Open https://github.com/new in your browser
2. Fill in repository details:
   - **Repository name**: `smartoperation`
   - **Description**: `Task Management System with AI Risk Analysis - Backend (Spring Boot), Frontend (React), Cronjob (Node.js)`
   - **Visibility**: Select **Private** (recommended for private projects)
   - **Initialize with**: Leave unchecked
3. Click **"Create repository"** button

### Step 2.2: Copy your repository URL

After creation, you'll see:
```
https://github.com/YOUR-USERNAME/smartoperation.git
```

Replace `YOUR-USERNAME` with your actual GitHub username.

---

## Part 3: Connect Local Repository to GitHub

### Step 3.1: Add remote and set main branch

```powershell
# Add GitHub remote
git remote add origin https://github.com/YOUR-USERNAME/smartoperation.git

# Rename branch to main (if not already)
git branch -M main

# Verify remote is added
git remote -v
```

**Expected output:**
```
origin  https://github.com/YOUR-USERNAME/smartoperation.git (fetch)
origin  https://github.com/YOUR-USERNAME/smartoperation.git (push)
```

---

## Part 4: Commit and Push Your Code

### Step 4.1: Add all files to git

```powershell
# Check status first
git status

# Add all files
git add .

# Verify files are staged
git status
```

### Step 4.2: Create initial commit

```powershell
git commit -m "Initial commit: Complete Smartoperation application

- Backend: Spring Boot REST API with JWT, Rate Limiting, WebSocket
- Frontend: React TypeScript with Vite
- Database: MySQL with Docker
- Cronjob: Node.js AI Risk Analysis (runs every minute)
- Docker Compose: Full stack containerization
- CI/CD: GitHub Actions workflow configured"
```

### Step 4.3: Push to GitHub

```powershell
# Push with upstream tracking
git push -u origin main

# For subsequent pushes, just use:
# git push
```

**If prompted for authentication:**
- Option 1: Use GitHub Personal Access Token (recommended)
  - Go to GitHub Settings → Developer settings → Personal access tokens
  - Generate new token with `repo` scope
  - Paste token when prompted
  
- Option 2: Use GitHub SSH key
  - Set up SSH key: https://docs.github.com/en/authentication/connecting-to-github-with-ssh

---

## Part 5: Verify Push Was Successful

### Step 5.1: Check on GitHub

1. Go to https://github.com/YOUR-USERNAME/smartoperation
2. Verify you can see:
   - All your code files
   - `.github/workflows/main.yml` (CI/CD pipeline)
   - `docker-compose.yml`
   - Commit message in commit history

### Step 5.2: Verify locally

```powershell
# Check remote tracking
git remote -v

# View commit log
git log --oneline

# Check current branch
git branch -a
```

---

## Part 6: CI/CD Pipeline Setup

### Step 6.1: Verify GitHub Actions is enabled

1. Go to your repository → **Actions** tab
2. You should see "CI/CD Pipeline" workflow
3. It will automatically run on next push to `main` branch

### Step 6.2: Add Docker Hub credentials (Optional, for publishing images)

If you want to auto-push Docker images:

1. Go to your repository → **Settings** → **Secrets and variables** → **Actions**
2. Click **New repository secret** for each:

   ```
   DOCKERHUB_USERNAME
   value: your-docker-username
   
   DOCKERHUB_TOKEN
   value: your-docker-personal-access-token
   ```

3. Update `.github/workflows/main.yml` to include Docker push step

### Step 6.3: Monitor workflow runs

1. After each push, go to **Actions** tab
2. Click on the workflow run to see:
   - Build status (✅ or ❌)
   - Build logs
   - Test results

---

## Part 7: Regular Git Workflow

### Making changes and pushing updates

```powershell
# 1. Make changes to your files

# 2. Check what changed
git status

# 3. Stage changes
git add .
# Or specific files:
# git add backend/src/main/java/com/smartoperation/controller/AuthController.java

# 4. Commit with descriptive message
git commit -m "Fix: Add test@gmail.com user authentication

- Updated AuthController password validation
- Added null-safe password comparison
- Verified with test user login"

# 5. Push to GitHub
git push
```

---

## Part 8: Troubleshooting

### Error: "fatal: not a git repository"
**Solution**: Make sure you're in the project directory with `git init` run:
```powershell
cd "c:\GMU\Summer 2024\Projects\Smartoperation"
git init
```

### Error: "Permission denied (publickey)"
**Solution**: Use HTTPS instead of SSH, or set up SSH keys:
```powershell
git remote set-url origin https://github.com/YOUR-USERNAME/smartoperation.git
```

### Error: "The current branch main has no upstream branch"
**Solution**: Use the `-u` flag when pushing:
```powershell
git push -u origin main
```

### Error: "Updates were rejected because the tip of your branch is behind"
**Solution**: Pull latest changes first:
```powershell
git pull origin main
git push origin main
```

---

## Part 9: Additional GitHub Features

### Enable branch protection rules (Optional)

1. Go to **Settings** → **Branches**
2. Add rule for `main`:
   - Require pull request reviews
   - Require status checks to pass
   - Dismiss stale pull requests

### Enable GitHub Pages (Optional, for documentation)

1. Go to **Settings** → **Pages**
2. Set source to `main` branch `/root`
3. Your docs will be available at: `https://YOUR-USERNAME.github.io/smartoperation`

---

## Part 10: Useful Git Commands

```powershell
# View all commits
git log --oneline --all

# View changes not yet committed
git diff

# View staged changes
git diff --cached

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Discard changes to a file
git checkout -- filename

# Create a new branch
git checkout -b feature/new-feature

# Switch branches
git checkout main

# Merge a branch
git merge feature/new-feature

# Delete a branch
git branch -d feature/new-feature

# Tag a release
git tag -a v1.0.0 -m "Version 1.0.0 release"
git push origin v1.0.0
```

---

## Quick Reference Commands

```powershell
# One-time setup (from project directory)
git init
git config user.name "Your Name"
git config user.email "your-email@gmail.com"
git remote add origin https://github.com/YOUR-USERNAME/smartoperation.git
git branch -M main
git add .
git commit -m "Initial commit: Smartoperation application"
git push -u origin main

# Regular workflow
git add .
git commit -m "Your message"
git push
```

---

## Next Steps

1. ✅ Complete Part 1-5 above to push code
2. Monitor your GitHub Actions workflow
3. Set up branch protection rules
4. Invite team members as collaborators
5. Create issues and pull requests for features
6. Set up project board for task tracking

---

**Created**: April 25, 2026  
**For**: Smartoperation Project  
**Author**: Development Team
