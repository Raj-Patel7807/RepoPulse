# RepoPulse 🚀

RepoPulse is a GitHub-inspired version control system built using Java and PostgreSQL. (Terminal Based Application)

The project simulates core functionalities of modern collaborative development platforms such as:
- repository management
- branches and commits
- pull requests
- issue tracking
- discussions and reviews
- releases and tags
- notifications and activity logs

RepoPulse focuses heavily on relational database design, normalization, modular architecture, and transaction-safe operations.

---

# Features ✨

## Authentication & User Management
- User registration and login
- Profile management
- User following system
- User blocking and reporting
- Pinned repositories

---

## Repository Management
- Create public/private/internal repositories
- Fork repositories
- Repository collaborators with role-based access
- Repository stars and watchers

---

## Branch & Commit System
- Branch creation and management
- Commit history tracking
- Parent-child commit relationships

---

## Pull Request Workflow
- Create pull requests
- Pull request reviews
- Review comments
- Issue linking
- Merge tracking

---

## Issue Tracking System
- Repository issues
- Labels and milestones
- Issue assignment
- Status management

---

## Discussion System
- Threaded discussions
- Commit discussions
- Pull request discussions
- Issue comments

---

## Releases & Tags
- Repository tags
- Release creation
- Release notes support

---

## File Versioning
- Repository file tracking
- File versions
- File diffs
- Change type tracking

---

# Tech Stack 🛠️

| Technology | Usage                                |
|---|--------------------------------------|
| Java | Simple Terminal base App development |
| PostgreSQL | Relational database                  |
| JDBC | Database connectivity                |
| Maven | Dependency management                |
| SQL | Schema and query design              |

---

# Project Structure 📂

```text
RepoPulse/
├── database/
│   ├── DDL.sql
│   ├── seed.sql
│   ├── DESIGN.md
│   ├── ERD.png
│   ├── normalization.md
│   └── README.md
│
├── src/main/java/com/repopulse/
│   ├── auth/
│   ├── branch/
│   ├── commit/
│   ├── common/
│   ├── discussion/
│   ├── file/
│   ├── infra/
│   ├── issue/
│   ├── notification/
│   ├── pullrequest/
│   ├── release/
│   ├── repository/
│   ├── user/
│   ├── App.java
│   └── Main.java
│
└── test/
```

---

# Architecture 🏗️

RepoPulse follows a layered modular architecture:

```text
CLI Layer
    ↓
Service Layer
    ↓
DAO Layer
    ↓
PostgreSQL Database
```

### Layers

| Layer | Responsibility |
|---|---|
| CLI | User interaction and menus |
| Service | Business logic and transaction flow |
| DAO | SQL queries and database operations |
| Model | Entity representation |
| Validator | Input and rule validation |

---

# Database Design 🗄️

The database schema is one of the core strengths of RepoPulse.

### Highlights
- 25+ relational tables
- normalized schema
- composite keys
- many-to-many mappings
- self-referencing relationships

### SQL Concepts Used
- Primary Keys
- Foreign Keys
- CHECK Constraints
- UNIQUE Constraints
- Composite Primary Keys
- Junction Tables
- JSON columns

---

# Normalization 📘

The schema is designed using normalization principles to:
- reduce redundancy
- maintain consistency
- improve scalability
- avoid update anomalies

Most tables satisfy:
- 1NF
- 2NF
- 3NF

Many relations also satisfy BCNF.

---

# Setup Instructions ⚙️

## 1. Clone Repository

```bash
git clone <your-repo-url>
cd RepoPulse
```

---

## 2. Create PostgreSQL Database

```sql
CREATE DATABASE repopulse;
```

---

## 3. Run Database Schema

```bash
psql -U postgres -d repopulse -f database/DDL.sql
```

---

## 4. Insert Seed Data

```bash
psql -U postgres -d repopulse -f database/seed.sql
```

---

## 5. Configure Database

Rename:

```text
src/main/resources/config.properties.example
```

to:

```text
config.properties
```

Then update:
- database URL
- username
- password

---

## 6. Run Project

```bash
mvn clean install
mvn exec:java
```

---

# Design Goals 🎯

RepoPulse was built to demonstrate:
- DBMS concepts
- relational schema design
- layered architecture
- modular backend structure
- scalable repository modeling

---

# Future Improvements 🚀

- Web-based frontend
- REST API support
- Repository analytics dashboard
- Real-time notifications
- Code review enhancements

---

# Author 👨‍💻

Raj Patel

Built as a DBMS project inspired by real-world version control platforms like GitHub.
