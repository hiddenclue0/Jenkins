# 🔍 SonarQube Code Analysis Integration with Jenkins

This README documents how I integrated **SonarQube Code Analysis** into my Jenkins CI pipeline for the `vprofile-project` using declarative pipeline and best practices.

---

## 🚀 Overview

SonarQube helps analyze code quality, detect bugs, vulnerabilities, and improve overall software hygiene. This integration allows Jenkins to scan code and upload the results to the SonarQube server during each CI build.

---

## 🧱 Step-by-Step Setup

### 1. ✅ Prerequisites

- Jenkins Server (pre-installed)
- SonarQube Server (running on EC2 with NGINX on port 80)
- Jenkins and SonarQube must be in the **same VPC**
- Ports **80** (SonarQube) and **8080** (Jenkins) should allow internal communication via security group rules

---

### 2. ⚙️ Install SonarQube Scanner Tool in Jenkins

- Go to **Manage Jenkins → Global Tool Configuration**
- Scroll to **SonarQube Scanner** section
- Click **Add SonarQube Scanner**
  - **Name**: `Sonar 6.2`
  - Select: `SonarQube Scanner 6.2.14610`
- Click **Save**

> Note: This tool name will be referenced in the Jenkinsfile.

---

### 3. 🔐 Add SonarQube Server to Jenkins

- Go to **Manage Jenkins → Configure System**
- Scroll to **SonarQube Servers**
- Check **Enable injection of SonarQube server configuration as build environment variables**
- Click **Add SonarQube**
  - **Name**: `Sonar Server`
  - **Server URL**: `http://<PRIVATE-IP>` (of SonarQube instance)
  - **Server authentication token**: Add new Jenkins credentials

#### ➕ Generate SonarQube Token

- Log into SonarQube
- Click on user icon (top right) → **My Account → Security**
- Generate a **User Token** named `Jenkins`
- Copy and save this token

#### ➕ Add Jenkins Credentials

- In Jenkins: Click **Add → Jenkins**
- Choose **Kind**: Secret Text
- Paste the token
- ID: `sonar-token`
- Description: `SonarQube Authentication Token`

Select this token when adding the server.

Click **Save**.

---

### 4. 🔒 Security Group Configuration

Ensure the **SonarQube Security Group** has:

- Inbound rule: `Port 80 → Source: Jenkins Security Group`
- Jenkins can then securely access SonarQube using internal networking.

---

## ✅ What's Next?

You’ve now completed SonarQube-Jenkins integration. In the next step, I’ll write pipeline code using the `SonarQube Scanner` tool to perform code analysis and upload the results automatically during CI runs.

---

## 📘 Summary

| Component         | Configuration                            |
|------------------|-------------------------------------------|
| SonarQube Scanner| Installed as `Sonar 6.2` in Jenkins       |
| Server Name      | `Sonar Server`                            |
| Server URL       | `http://<PRIVATE-IP>`                     |
| Token Type       | Secret Text in Jenkins Credentials        |
| Jenkins Access   | Internal via Security Group on Port 80    |

---

> 🧠 Reminder: Always use **private IPs** for intra-VPC communications to stay secure and avoid public exposure.