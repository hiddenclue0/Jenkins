# 🔍 SonarQube Server Setup on AWS EC2

This document explains how I deployed **SonarQube** on an EC2 Ubuntu instance with NGINX as a frontend and integrated it with Jenkins for automated code analysis.

---

## 🚀 Launching the EC2 Instance

I started by launching a new EC2 instance with the following configuration:

- **Name**: Sonar Server
- **AMI**: Ubuntu 24.04 LTS (free tier eligible)
- **Instance Type**: t2.medium (as SonarQube needs at least 4GB of RAM)
- **Key Pair**: I created a new key pair and named it `Sonar-key`
- **Security Group**: I created a new security group named `sonar-sg` with these rules:
  - Allowed **SSH (port 22)** access from my IP
  - Allowed **HTTP (port 80)** access from my IP
  - Allowed **HTTP (port 80)** access from the Jenkins security group so Jenkins can push analysis results

During the launch, in the **Advanced Details**, I pasted the content of my `sonar-setup.sh` script into the **User Data** section to automate the installation and configuration of SonarQube.

---

## 🔧 Accessing SonarQube

Once the instance was up and the setup completed, I accessed the SonarQube interface:

1. I copied the public IP of the instance and opened `http://<Public-IP>` in my browser (port 80 was already used by NGINX).
2. I logged in using the default credentials:
   - Username: `admin`
   - Password: `admin`
3. After the first login, I was prompted to change the default password, which I did.

---

## 🔄 Jenkins ↔ SonarQube Integration

To integrate Jenkins with SonarQube:

- Jenkins needs to push code analysis results to SonarQube over **port 80**, which was already allowed.
- Additionally, **SonarQube needs to communicate back to Jenkins** (e.g., to report test results or analysis status). So, I edited the **Jenkins Security Group** and added an inbound rule:
  - Allowed **port 8080** access from the `sonar-sg` (SonarQube Security Group)

This completed the mutual integration setup.

---

## 📊 Dashboard

After everything was configured:

- I was able to see the SonarQube dashboard showing metrics such as bugs, vulnerabilities, code smells, and technical debt.
- Once integrated with Jenkins, all pipeline jobs automatically pushed analysis results to SonarQube, which helped me visualize the code quality in real time.
