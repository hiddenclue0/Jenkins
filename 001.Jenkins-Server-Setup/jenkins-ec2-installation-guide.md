# 🚀 Jenkins Installation on AWS EC2 (Ubuntu/Debian)

This guide explains how I deployed **Jenkins** on an **AWS EC2 instance** running **Ubuntu 24.04 LTS** (or Debian). It includes everything from EC2 launch to Jenkins installation and setup using a simple shell script.

---

## ☁️ 1. Launch EC2 Instance

I launched a new EC2 instance with the following configuration:

- **Name**: `Jenkins Server`
- **AMI**: Ubuntu Server 24.04 LTS
- **Instance Type**: `t2.medium` (or `t3.medium` for better performance)
- **Key Pair**: Created a new key pair named `Jenkins-server-KP.pem`
- **Security Group**: Created a new group named `jenkins-sg` with these inbound rules:

| Port | Protocol | Source        | Purpose         |
|------|----------|---------------|-----------------|
| 22   | TCP      | My IP only    | SSH access      |
| 8080 | TCP      | My IP only    | Jenkins Web UI  |

---

## 🔐 2. Connect to EC2 via SSH

From my terminal, I used the following command:

```bash
ssh -i "Jenkins-server-KP.pem" ubuntu@<your-ec2-public-ip>
```

> Replace `<your-ec2-public-ip>` with your instance’s actual public IP address.

---

## ⚙️ 3. Jenkins Installation Script

Instead of installing everything manually, I created a script named `jenkins-setup.sh` to automate the entire process.

### 📄 `jenkins-setup.sh`

```bash
#!/bin/bash

# Update system packages
sudo apt update

# Install Java (JDK 17 and 21)
sudo apt install openjdk-17-jdk -y
sudo apt install openjdk-21-jdk -y

# Add Jenkins GPG key
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

# Add Jenkins repository
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
https://pkg.jenkins.io/debian-stable binary/" | sudo tee \
/etc/apt/sources.list.d/jenkins.list > /dev/null

# Update and install Jenkins
sudo apt-get update
sudo apt-get install jenkins -y
```

---

## ▶️ 4. Start and Enable Jenkins

```bash
sudo systemctl start jenkins
sudo systemctl enable jenkins
sudo systemctl status jenkins
```

---

## 🌐 5. Access Jenkins

1. Open your browser and go to: `http://<your-ec2-public-ip>:8080`
2. Retrieve the initial admin password:

```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

3. Complete setup with the following credentials:

   - **Install suggested plugins**
   - **Username:** `jenkins`
   - **Password:** `jenkins123`
   - **Confirm password:** `jenkins123`
   - **Full name:** `jenkins`
   - **E-mail address:** `jenkins@gmail.com`
   - **Jenkins URL:** `http://jenkins:8080/`

---

---

## 🗂️ Recommended Repository Structure

```
Jenkins/
└── 1.Installation/
    ├── jenkins-setup.sh
    └── jenkins-ec2-installation-guide.md
```

---

## ✅ Summary

- Automated Jenkins setup using a shell script.
- Secure EC2 launch with proper security group rules.
- Ready-to-use repository structure for DevOps projects.