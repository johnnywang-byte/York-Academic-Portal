# York Academic Portal (YAP) 🎓

> A modern, full-stack university management system featuring AI-powered assistance, role-based access control, and seamless data visualization.

![Java](https://img.shields.io/badge/Java-Spring%20Boot-green)
![Vue.js](https://img.shields.io/badge/Vue.js-3.0-4FC08D)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![AI](https://img.shields.io/badge/AI-Google%20Gemini-orange)

## 📖 Introduction

**York Academic Portal** is a comprehensive web application designed to digitize and streamline administrative processes for educational institutions. It bridges the gap between students, faculty, and administrators through a unified interface.

Key highlights include an **AI Assistant** (powered by Google Gemini) that helps users retrieve information intelligently, and a robust **RESTful API** backend that ensures data integrity and security.

## 🚀 Key Features

* **👥 Role-Based Management**: Distinct portals and permissions for Students, Employees, and Administrators.
* **🤖 AI-Powered Support**: Integrated **Google Gemini AI** (`AiService`) to provide intelligent responses to user queries and automate support.
* **📊 Data Visualization**: Dynamic dashboards for Department and Employee log management.
* **🔒 Secure Authentication**: Robust login system with session management.
* **📡 RESTful Architecture**: Clean separation of concerns with a Spring Boot backend and Vue.js frontend.

## 🛠 Tech Stack

### Backend
* **Framework**: Java Spring Boot (Web, AOP)
* **Database**: MySQL & MyBatis (Persistence Layer)
* **AI Integration**: Google Gemini API
* **Tools**: Maven, Lombok

### Frontend
* **Framework**: Vue.js 3 (Composition API)
* **Build Tool**: Vite
* **Styling**: CSS3, Modern UI Components
* **State Management**: Vue Reactive System

---

## 💻 Getting Started

Follow these steps to set up the project locally.

### Prerequisites
* **JDK 17** or higher
* **Node.js** (v16+) & npm
* **MySQL** Database

### 1. Database Setup
1.  Create a MySQL database named `academic_portal`.
2.  Import the schema and initial data:
    ```bash
    mysql -u root -p academic_portal < academic-portal/portal.sql
    ```

### 2. Backend Setup (Spring Boot)
Navigate to the backend directory:
```bash
cd academic-portal

```

**Configuration**:

* Open `src/main/resources/application.yml`.
* Update your MySQL username/password.
* Ensure your `AiService.java` has a valid API Key.

Run the application:

```bash
# Using Maven wrapper (if available) or your IDE
mvn spring-boot:run

```

*The backend server typically runs on `http://localhost:8080`.*

### 3. Frontend Setup (Vue.js)

Open a new terminal and navigate to the frontend directory:

```bash
cd vue-academic-portal

```

Install dependencies and run:

```bash
npm install
npm run dev

```

*Access the application at `http://localhost:5173` (or the port shown in terminal).*

---

## 📂 Project Structure

```text
Project2026/
├── .gitignore                 # Git configuration (Ignores logs, target/, node_modules/)
├── academic-portal/           # Backend (Spring Boot)
│   ├── src/main/java/         # Controllers, Services, AI Logic
│   ├── src/main/resources/    # Config (application.yml), Mappers
│   └── portal.sql             # Database initialization script
│
├── vue-academic-portal/       # Frontend (Vue 3 + Vite)
│   ├── src/                   # Components, Views, Assets
│   ├── package.json           # Frontend dependencies
│   └── vite.config.js         # Build configuration
│
└── README.md                  # Project Documentation

```

## 🚧 Future Improvements

* [ ] Implement JWT (JSON Web Token) for stateless authentication.
* [ ] Containerization using **Docker**.
* [ ] Enhance AI features to include personalized course recommendations.

## 👨‍💻 Author

**Jiyuan (Johnny) Wang**

* Computer Science Student @ York University
* [LinkedIn](https://www.linkedin.com/in/johnny-wang-652820337/)
* [GitHub](https://github.com/johnnywang-byte)

---

*This project was developed for academic purposes and demonstrates full-stack development capabilities.*