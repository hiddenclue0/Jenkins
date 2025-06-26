# 📦 Nexus Repository Manager Setup on AWS EC2

This guide walks through deploying **Nexus Repository Manager** on an EC2 instance using Amazon Linux 2023 with proper security and automation.

---

## 🚀 Launching the EC2 Instance

- Launch an EC2 instance with:
  - **Name**: Nexus Server
  - **AMI**: Amazon Linux 2023
  - **Instance Type**: t2.medium or t3.medium
  - **Key Pair**: Create a new key pair (e.g., `Nexus-key`)
  - **Security Group**: Create a new security group (`nexus-sg`) with:
    - Port **22**: Allow SSH from your IP
    - Port **8081**: Allow access from your IP
    - Port **8081**: Allow access from **Jenkins Security Group**

- Paste the contents of your `nexus-setup.sh` in **User Data** to automate setup.

---

## 🔧 Accessing Nexus

1. SSH into the instance:
   ```bash
   ssh -i Nexus-key.pem ec2-user@<Public-IP>
   ```

2. Switch to root and check service:
   ```bash
   sudo su
   systemctl status nexus
   ```

3. Open in browser: `http://<Public-IP>:8081`

4. Get initial admin password:
   ```bash
   cat /opt/sonatype-work/nexus3/admin.password
   ```

5. Log in with:
   - Username: `admin`
   - Password: (paste copied password)

6. Set a new password (e.g., `admin123`) and enable **anonymous access**.

---

## ✅ Notes

- Ensure VPN or proxies are turned off when accessing Nexus via browser.
- Nexus stores artifacts uploaded from Jenkins.
- Credentials like `admin/admin123` will be reused in Jenkins pipelines.

---

## 📁 Home Directory & Tools

- Nexus home: `/opt/nexus`
- Java version: Java 17 (preinstalled by `nexus-setup.sh`)