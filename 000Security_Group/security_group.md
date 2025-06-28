# 🔐 My Security Group Setup for Jenkins CI/CD Infrastructure

This README documents how I configured the security groups for my Jenkins-based CI/CD pipeline, which integrates with **Nexus** and **SonarQube**. I applied minimal access rules to enforce security while ensuring necessary service-to-service communication.

---

## 🚀 Jenkins Security Group (`jenkins-sg`)

**🔧 Description:**  
This group manages inbound access to my Jenkins server, which orchestrates the CI/CD pipeline and communicates with Nexus and SonarQube.

I created a security group called `jenkins-sg` with the following rules:

| Port | Protocol | Source                | Purpose                                 |
|------|----------|-----------------------|-----------------------------------------|
| 22   | TCP      | My IP Address         | SSH access                              |
| 8080 | TCP      | My IP Address         | Jenkins web UI                          |
| 8080 | TCP      | SonarQube SG (ID)     | Webhook & analysis communication        |
| 8080 | TCP      | Nexus SG (ID)         | Allow Nexus to trigger Jenkins (if needed) |

> Port 8080 is restricted externally to my IP, but internally allows SonarQube and Nexus to communicate with Jenkins for webhook callbacks or artifact triggers if required.

---

## 📦 Nexus Security Group (`nexus-sg`)

**📤 Description:**  
This group governs access to the Nexus repository, which Jenkins uses to upload and manage build artifacts.

Here’s how I configured the `nexus-sg`:

| Port | Protocol | Source                | Purpose                                 |
|------|----------|-----------------------|-----------------------------------------|
| 22   | TCP      | My IP Address         | SSH access                              |
| 8081 | TCP      | My IP Address         | Nexus web UI                            |
| 8081 | TCP      | Jenkins SG (ID)       | Allow Jenkins to upload artifacts       |
| 8081 | TCP      | SonarQube SG (ID)     | Allow SonarQube to fetch dependencies (if needed) |

> Jenkins requires access to Nexus on port 8081 to push build artifacts. Optionally, SonarQube can access Nexus if it needs to fetch dependencies for analysis.

---

## 🧪 SonarQube Security Group (`sonar-sg`)

**🧹 Description:**  
This group controls access to the SonarQube server, used for static code analysis triggered by Jenkins.

Here’s how I set up `sonar-sg`:

| Port | Protocol | Source                | Purpose                                 |
|------|----------|-----------------------|-----------------------------------------|
| 22   | TCP      | My IP Address         | SSH access                              |
| 9000 | TCP      | My IP Address         | SonarQube web UI                        |
| 9000 | TCP      | Jenkins SG (ID)       | Jenkins to send code analysis results   |
| 9000 | TCP      | Nexus SG (ID)         | Allow Nexus to trigger analysis (if needed) |

> Port 9000 is open to Jenkins internally so it can post analysis results to the SonarQube server. Optionally, Nexus can communicate if your workflow requires it.

---

## 🧩 Internal Communication & Network Topology

All three services — Jenkins, Nexus, and SonarQube — are deployed on separate EC2 instances within the **same VPC**, allowing them to communicate over private IPs using their security groups. Here’s how the port exposure is managed across services:

| Service     | Exposed Ports | Access Scope                                 |
|-------------|--------------|----------------------------------------------|
| Jenkins     | 8080         | My IP + SonarQube SG (8080) + Nexus SG (8080)|
| Nexus       | 8081         | My IP + Jenkins SG (8081) + SonarQube SG (8081)|
| SonarQube   | 9000         | My IP + Jenkins SG (9000) + Nexus SG (9000)  |

---

## ✅ Summary

This configuration follows the **principle of least privilege**:
- Public access is tightly restricted to my IP address.
- Only necessary ports are open.
- Internal service communication is enabled via scoped security group rules.
- All instances are in the same VPC to reduce latency and enhance private networking.

I'll regularly review and audit these rules to keep the setup secure and efficient.

---
