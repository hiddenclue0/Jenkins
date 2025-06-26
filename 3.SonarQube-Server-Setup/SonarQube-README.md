# 🔍 SonarQube Server Setup on AWS EC2

This guide helps deploy **SonarQube** on an EC2 Ubuntu instance with NGINX frontend and Jenkins integration.

---

## 🚀 Launching the EC2 Instance

- Launch an EC2 instance with:
  - **Name**: Sonar Server
  - **AMI**: Ubuntu 24.04 LTS (Free tier eligible)
  - **Instance Type**: t2.medium or t3.medium (requires ≥4GB RAM)
  - **Key Pair**: Create a new key pair (e.g., `Sonar-key`)
  - **Security Group**: Create a new security group (`sonar-sg`) with:
    - Port **22**: Allow SSH from your IP
    - Port **80**: Allow HTTP from your IP
    - Port **80**: Allow access from **Jenkins Security Group**

- Paste the contents of your `sonar-setup.sh` in **User Data** to automate setup.

---

## 🔧 Accessing SonarQube

1. Open browser and navigate to: `http://<Public-IP>`
2. Login with default credentials:
   - Username: `admin`
   - Password: `admin`
3. Reset password when prompted.

---

## 🔄 Jenkins ↔ SonarQube Integration

- Jenkins uploads analysis results to SonarQube on port **80**.
- SonarQube sends feedback to Jenkins on port **8080**.

### ✅ Update Jenkins Security Group

- Add an inbound rule:
  - Port **8080**: Source = `sonar-sg` (SonarQube SG)

---

## 📊 Dashboard

- SonarQube UI shows project health, bugs, vulnerabilities, and code smells.
- Jenkins jobs will automatically push scan results here.