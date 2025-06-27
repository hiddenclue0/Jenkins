# 🔐 My Security Group Setup for Jenkins CI/CD Infrastructure

This README documents how I configured the security groups for my Jenkins-based CI/CD pipeline, which integrates with Nexus and SonarQube. I’ve applied minimal access rules to keep the system secure while ensuring necessary communication between services.

---

## 🚀 Jenkins Security Group (`jenkins-sg`)

**🔧 Description:**  
Handles inbound access to the Jenkins server for CI/CD orchestration and provides connectivity to internal services like Nexus and SonarQube.

I created a security group named `jenkins-sg` with the following rules:

| Port | Protocol | Source         | Purpose        |
|------|----------|----------------|----------------|
| 22   | TCP      | My IP Address  | SSH access     |
| 8080 | TCP      | My IP Address  | Jenkins web UI |

> Jenkins runs on port 8080. I limited access to just my IP for security.

---

## 📦 Nexus Security Group (`nexus-sg`)

**📤 Description:**  
Allows secure access to the Nexus repository manager and enables Jenkins to publish build artifacts.

For Nexus, I set up a separate security group called `nexus-sg`:

| Port | Protocol | Source                | Purpose                          |
|------|----------|------------------------|----------------------------------|
| 22   | TCP      | My IP Address          | SSH access                       |
| 8081 | TCP      | My IP Address          | Nexus web UI                     |
| 8081 | TCP      | Jenkins Security Group | Allow Jenkins to upload artifacts |

> Jenkins needs access to Nexus on port 8081 to upload artifacts during the CI/CD process, so I allowed access from the Jenkins security group.

---


## 🧪 SonarQube Security Group (`sonar-sg`)

**🧹 Description:**  
Manages access to the SonarQube server for code quality analysis, including secure interaction with Jenkins.

Lastly, I created a `sonar-sg` security group for SonarQube:

| Port | Protocol | Source                | Purpose                              |
|------|----------|------------------------|--------------------------------------|
| 22   | TCP      | My IP Address          | SSH access                           |
| 80   | TCP      | My IP Address          | SonarQube web UI                     |
| 80   | TCP      | Jenkins Security Group | Allow Jenkins to push analysis results |

> Jenkins sends code quality reports to SonarQube, so I opened HTTP access (port 80) for the Jenkins group too.

---

## ✅ Summary

This setup follows the principle of **least privilege**:
- I only open essential ports.
- SSH and web UI access are restricted to my IP.
- Jenkins can interact securely with Nexus and SonarQube.

I'll make sure to update the IPs if mine changes and regularly review the rules for unnecessary access.

---
