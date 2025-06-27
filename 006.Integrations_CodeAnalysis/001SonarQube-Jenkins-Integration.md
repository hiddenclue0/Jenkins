# 🔍 SonarQube Code Analysis Integration with Jenkins

In this project, I integrated **SonarQube Code Analysis** into my Jenkins CI pipeline for the `vprofile-project`. I used a declarative pipeline approach and followed industry best practices to ensure a reliable and automated code quality check during each CI build.

---

## 🚀 Overview

SonarQube is a powerful tool that helps identify code quality issues, security vulnerabilities, and bugs. By integrating it with Jenkins, I was able to automatically scan my project's codebase and upload the analysis results to a SonarQube server on every code change.

---

## 🧱 Step-by-Step Setup

### 1. ✅ Prerequisites

Before starting the integration, I ensured the following components were in place:

- A running Jenkins server.
- A SonarQube server hosted on an EC2 instance, reverse proxied via NGINX (available on port 80).
- Both Jenkins and SonarQube were deployed in the same **VPC**.
- Security groups allowed internal communication:
  - **Port 80** open for SonarQube.
  - **Port 8080** open for Jenkins.

---

### 2. ⚙️ Installing SonarQube Scanner in Jenkins

To enable Jenkins to analyze code using SonarQube, I installed the SonarQube Scanner tool:

- I navigated to **Manage Jenkins → Global Tool Configuration**.
- Scrolled down to the **SonarQube Scanner** section.
- Clicked **Add SonarQube Scanner**.
  - Set the **Name** to `Sonar 6.2`.
  - Selected version `SonarQube Scanner 6.2.14610`.
- Clicked **Save**.

> This scanner tool name (`Sonar 6.2`) is later referenced in the `Jenkinsfile`.

---

### 3. 🔐 Configuring SonarQube Server in Jenkins

Next, I connected Jenkins to my SonarQube server:

- Went to **Manage Jenkins → Configure System**.
- Located the **SonarQube Servers** section.
- Checked the box to **Enable injection of SonarQube server configuration as build environment variables**.
- Clicked **Add SonarQube** and entered the following:
  - **Name**: `sonarserver`
  - **Server URL**: `http://<PRIVATE-IP>:80` (SonarQube EC2 instance)
  - **Server authentication token**: Added via Jenkins credentials

#### 🔑 Generating a SonarQube Token

To authenticate securely:

- I logged into the SonarQube web UI.
- Clicked on the profile icon → **My Account → Security**.
- Generated a token named `jenkins` and copied it for use in Jenkins.

#### ➕ Adding the Token in Jenkins Credentials

- In Jenkins, I clicked **Add → Jenkins** under credentials.
- Selected **Kind**: Secret Text.
- Pasted the SonarQube token.
- Set **ID**: `sonar-token`.
- Set a description like: `SonarQube Authentication Token`.

I then selected this token when adding the SonarQube server config.

Finally, I clicked **Save** to apply the changes.

---

### 4. 🔒 Security Group Configuration

To allow secure internal access:

- I edited the **SonarQube EC2 Security Group**.
- Added an **Inbound Rule**:
  - **Port**: 80
  - **Source**: Jenkins Security Group ID

This allowed Jenkins to communicate with SonarQube securely via private IP within the same VPC.

---

## ✅ What’s Next?

With the setup complete, I proceeded to write a Jenkins pipeline script using the `SonarQube Scanner` tool. This script runs code analysis as part of the CI workflow and pushes the results to SonarQube automatically on each build.

---

## 📘 Summary

| Component         | Configuration                            |
|------------------|-------------------------------------------|
| SonarQube Scanner| Installed as `Sonar 6.2` in Jenkins       |
| Server Name      | `sonarserver`                             |
| Server URL       | `http://<PRIVATE-IP>`                     |
| Token Type       | Secret Text (Jenkins Credential)          |
| Jenkins Access   | Internal via Security Group on Port 80    |

---

> 🧠 Tip: Always use **private IP addresses** for internal communication between Jenkins and SonarQube to enhance security and reduce exposure to the public internet.
