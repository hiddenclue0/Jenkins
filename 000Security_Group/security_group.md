# 🔐 My Security Group Setup for Jenkins CI/CD Infrastructure

This README documents how I configured the security groups for my Jenkins-based CI/CD pipeline, which integrates with **Nexus** and **SonarQube**. I applied minimal access rules to enforce security while ensuring necessary service-to-service communication.

---

## 🚀 Jenkins Security Group (`jenkins-sg`)

**🔧 Description:**  
This group manages inbound access to my Jenkins server, which orchestrates the CI/CD pipeline and communicates with Nexus and SonarQube.

I created a security group called `jenkins-sg` with the following rules:

| Port | Protocol | Source         | Purpose        |
|------|----------|----------------|----------------|
| 22   | TCP      | My IP Address  | SSH access     |
| 8080 | TCP      | My IP Address  | Jenkins web UI |

To enhance integration within the pipeline, Jenkins also needs to communicate with SonarQube and vice versa. To support this, I added an internal rule:

| Port | Protocol | Source             | Purpose                          |
|------|----------|--------------------|----------------------------------|
| 8080 | TCP      | SonarQube SG (ID)  | Webhook & analysis communication |

> I restricted external access to port 8080 (Jenkins UI) to my IP only, while internally allowing SonarQube to communicate with Jenkins on the same port for webhook callbacks.

---

## 📦 Nexus Security Group (`nexus-sg`)

**📤 Description:**  
This group governs access to the Nexus repository, which Jenkins uses to upload and manage build artifacts.

Here’s how I configured the `nexus-sg`:

| Port | Protocol | Source                | Purpose                           |
|------|----------|------------------------|-----------------------------------|
| 22   | TCP      | My IP Address          | SSH access                        |
| 8081 | TCP      | My IP Address          | Nexus web UI                      |
| 8081 | TCP      | Jenkins Security Group | Allow Jenkins to upload artifacts |

> Jenkins requires access to Nexus on port 8081 to push build artifacts. I allowed internal access from the Jenkins SG for this purpose.

---

## 🧪 SonarQube Security Group (`sonar-sg`)

**🧹 Description:**  
This group controls access to the SonarQube server, used for static code analysis triggered by Jenkins.

Here’s how I set up `sonar-sg`:

| Port | Protocol | Source                | Purpose                                 |
|------|----------|------------------------|-----------------------------------------|
| 22   | TCP      | My IP Address          | SSH access                              |
| 80   | TCP      | My IP Address          | SonarQube web UI                        |
| 80   | TCP      | Jenkins Security Group | Jenkins to send code analysis results   |

> Port 80 is open to Jenkins internally so it can post analysis results to the SonarQube server.

---

## 🧩 Internal Communication & Network Topology

All three services — Jenkins, Nexus, and SonarQube — are deployed on separate EC2 instances within the **same VPC**, allowing them to communicate over private IPs using their security groups. Here’s how the port exposure is managed across services:

| Service     | Exposed Ports | Access Scope               |
|-------------|----------------|-----------------------------|
| Jenkins     | 8080           | My IP + SonarQube SG (8080) |
| Nexus       | 8081           | My IP + Jenkins SG (8081)   |
| SonarQube   | 80, 9000       | My IP + Jenkins SG (80)     |

---

## ✅ Summary

This configuration follows the **principle of least privilege**:
- Public access is tightly restricted to my IP address.
- Only necessary ports are open.
- Internal service communication is enabled via scoped security group rules.
- All instances are in the same VPC to reduce latency and enhance private networking.

I'll regularly review and audit these rules to keep the setup secure and efficient.

---
