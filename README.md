
# 🚀 vProfileApp CI/CD Automation with Jenkins

This project automates the build, analysis, and deployment process of the `vProfileApp` — a multi-tier, enterprise-ready Java web application — using Jenkins CI/CD pipeline integrated with **Nexus**, **SonarQube**, and **Slack**. This is a continuation of previous vProfileApp deployment projects and brings DevOps automation into action.

---

## 📘 Project Description

`vProfileApp` is an enterprise Java application built with:

- **Spring MVC & Spring Security**
- **JSP views**
- **MySQL** for persistence
- **Memcached** for caching
- **RabbitMQ** for messaging
- **Elasticsearch** for search capabilities

---

## 📂 Previous Work

This automation builds on the foundation of three completed projects:

1. [`vprofileApp-deployment-manually`](https://github.com/hiddenclue0/vprofileApp-deployment-manually.git)  
   Manual setup of multi-tier architecture using Vagrant and VirtualBox.

2. [`vprofileApp-Lift-and-Shift-AWS`](https://github.com/hiddenclue0/vprofileApp-Lift-and-Shift-AWS.git)  
   Migrated the app to AWS infrastructure using EC2, ALB, S3, Route 53, etc.

3. [`vprofileApp-aws-refactor-paas-saas`](https://github.com/hiddenclue0/vprofileApp-aws-refactor-paas-saas.git)  
   Refactored to AWS managed services using Beanstalk, RDS, ElastiCache, and MQ.

---

## ⚙️ Project Steps

### 1️⃣ Launch and Setup Jenkins, Nexus & SonarQube on EC2

Each service is provisioned on an EC2 instance using **user-data bash scripts**.

- **Jenkins Setup Script** (Ubuntu + Java 17)
- **Nexus Repository Setup Script** (Amazon Linux + Java 17)
- **SonarQube Setup Script** (Ubuntu + PostgreSQL + Java 17 + Nginx Proxy)

> All these scripts automate the environment setup and service configuration.

---

### 2️⃣ Configure Security Groups

Ensure proper **inbound rules** are configured to allow:

- Jenkins (port 8080)
- Nexus (port 8081)
- SonarQube (port 9000 via Nginx on 80)
- Inter-EC2 communication for build, analysis, artifact sharing

---

### 3️⃣ Jenkins Plugin Installation

Install necessary Jenkins plugins:

- Git Plugin
- Maven Integration Plugin
- SonarQube Scanner Plugin
- Nexus Artifact Uploader Plugin
- Slack Notification Plugin

---

### 4️⃣ Tool Integration with Jenkins

- **Nexus Integration**:  
  Save Nexus credentials and configure Nexus URL in Jenkins.

- **SonarQube Integration**:  
  Save SonarQube token in Jenkins credentials and configure SonarQube server in Jenkins global tools config.

SonarQube `sonar-project.properties` example:

```
sonar.projectKey=vprofile
sonar.projectName=vprofile-repo
sonar.projectVersion=1.0
sonar.sources=src/
sonar.java.binaries=target/test-classes/com/visualpathit/account/controllerTest/
sonar.junit.reportsPath=target/surefire-reports/
sonar.jacoco.reportsPath=target/jacoco.exec
sonar.java.checkstyle.reportPaths=target/checkstyle-result.xml
```

---

### 5️⃣ Jenkins Pipeline Creation

A Jenkins **Declarative Pipeline** is created that performs:

- Clone source from GitHub: [`vProfileApp`](https://github.com/hiddenclue0/vprofileApp.git)
- Compile using Maven
- Run Unit Tests and generate reports
- Perform static code analysis using SonarQube
- Publish artifacts to Nexus repository
- Notify results to Slack channel

> ✅ Note: The pipeline code is not included in this README for brevity.

---

### 6️⃣ Slack Integration

Slack is configured in Jenkins for:

- Build start/complete notifications
- Failure alerts for pipeline stages

You will need to create a **Slack App**, generate a **token**, and set up **Slack Webhook URL** in Jenkins credentials.

---

## 🧩 Final Outcome

At the end of this project:

- A fully automated CI/CD pipeline will be available via Jenkins.
- The build will be analyzed for quality (SonarQube), and artifacts will be stored (Nexus).
- Slack will notify build events.
- All infrastructure is provisioned via automation scripts.

---

## 📎 Repository Structure

```
vprofileApp-cicd-jenkins/
├── README.md
├── 1.Jenkins-Server-Setup/
│   ├── jenkins-install.sh
│   └── user-data-jenkins.sh
├── 2.Nexus-Server-Setup/
│   ├── nexus-install.sh
│   └── user-data-nexus.sh
├── 3.SonarQube-Server-Setup/
│   ├── sonarqube-install.sh
│   └── user-data-sonarqube.sh
├── 4.Security-Groups-and-Connections/
│   └── sg-rules.txt
├── 5.Jenkins-Plugin-Setup/
│   └── plugin-list.txt
├── 6.Integrations/
│   ├── jenkins-nexus.md
│   └── jenkins-sonarqube.md
├── 7.Pipeline-Script/
│   ├── Jenkinsfile
│   └── pipeline-steps.md
├── 8.Notifications/
│   └── email-notification-config.md
└── images/
    └── setup-architecture-diagram.png

```


---

## ✅ Prerequisites

Before running this project, ensure the following are installed and properly configured:

- **JDK 17**
- **Maven 3.9**
- **MySQL 8**

---

## 🛠️ Technologies Used

This project makes use of the following technologies:

- **Jakarta EE**
- **Spring MVC**
- **Spring Security**
- **Spring Data JPA**
- **Maven**
- **JSP**
- **Tomcat**
- **MySQL**
- **Memcached**
- **RabbitMQ**
- **Elasticsearch**

---

## 🗃️ Database Setup

The MySQL database is initialized using a dump file located at:

```
/src/main/resources/db_backup.sql
```

To import the database into your MySQL server:

```bash
mysql -u <username> -p <database_name> < src/main/resources/db_backup.sql
```

Replace `<username>` and `<database_name>` with your MySQL credentials and target database name.

---

---

## 🙌 Author

**Md Jakir Hosen**  
DevOps Projects | AWS | Jenkins | CI/CD 
🔗 [GitHub Profile](https://github.com/hiddenclue0)

---

