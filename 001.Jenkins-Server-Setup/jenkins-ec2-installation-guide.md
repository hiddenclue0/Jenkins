# 🚀 Jenkins CI/CD Setup on AWS EC2 (Ubuntu/Debian)

This guide walks you through setting up Jenkins on an AWS EC2 instance, covering security, disk management, and CI/CD pipeline integration with Maven, SonarQube, and Nexus.

---

## ☁️ 1. EC2 Instance Setup for Jenkins

**Instance Configuration:**

| Property       | Value                  |
|----------------|------------------------|
| Name           | Jenkins Server         |
| AMI            | Ubuntu 24.04 LTS       |
| Instance Type  | t2.medium or t3.medium |
| Key Pair       | Jenkins-server-KP.pem  |
| Security Group | jenkins-sg             |

**Security Group Inbound Rules (jenkins-sg):**

| Port | Protocol | Source        | Purpose            |
|------|----------|---------------|--------------------|
| 22   | TCP      | My IP only    | SSH access         |
| 8080 | TCP      | My IP only    | Jenkins Web UI     |
| 8080 | TCP      | SonarQube SG  | Webhook Callback   |

---

## 🔐 2. SSH Access to EC2

```bash
ssh -i "Jenkins-server-KP.pem" ubuntu@<your-ec2-public-ip>
```

---

## ⚙️ 3. Jenkins Installation via Shell Script

### 📄 jenkins-setup.sh

```bash
#!/bin/bash

sudo apt update
sudo apt install -y openjdk-17-jdk openjdk-21-jdk

sudo wget -O /usr/share/keyrings/jenkins-keyring.asc https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt-get update
sudo apt-get install -y jenkins
```

---

## ▶️ 4. Start Jenkins

```bash
sudo systemctl start jenkins
sudo systemctl enable jenkins
sudo systemctl status jenkins
```

---

## 🌐 5. Access Jenkins Web UI

- URL: http://<your-ec2-public-ip>:8080
- Unlock Jenkins:
   ```bash
   sudo cat /var/lib/jenkins/secrets/initialAdminPassword
   ```
- Create admin user:
   - Username: jenkins  
   - Password: jenkins123  
   - Email: jenkins@gmail.com
   - URL: http://jenkins:8080

---

## 🔌 6. Install Plugins

Install **Suggested Plugins** during setup, then add these via **Manage Jenkins > Plugins**:
- Maven Integration
- Pipeline Maven Integration
- GitHub Integration
- Pipeline: Stage View
- SonarQube Scanner
- Nexus Artifact Uploader
- Pipeline Utility Steps
- Build Timestamp

---

## 🛠️ 7. Manage Jenkins > Global Tool Configuration

Go to **Manage Jenkins > Global Tool Configuration**:

- Add JDK 17 and 21  
- Add Maven 3.9

Example:
```text
JDK:
   Name: JDK17
   Path: /usr/lib/jvm/java-17-openjdk-amd64
   Name: JDK21
   Path: /usr/lib/jvm/java-21-openjdk-amd64

Maven:
   Name: MAVEN3.9
   Auto install or provide binary path

```

## 🔄 8. Flow of Jenkins CI/CD Pipeline

1. Checkout source code from GitHub  
2. Build project using Maven  
3. Run test cases  
4. Analyze code with SonarQube  
5. Archive artifacts (.war)  
6. Upload artifacts to Nexus  

---

## 🧾 9. Jenkins Pipeline Code (Declarative)

```groovy
pipeline {
   agent any
   tools {
      maven "MAVEN3.9"
      jdk "JDK17"
   }
   stages {
      stage('Fetch code') {
            steps {
               git branch: 'main', url: 'https://github.com/hiddenclue0/vprofileApp-jenkins-cicd-automation.git'
            }
      }
      stage('UNIT TEST') {
            steps {
               sh 'mvn test'
            }
      }
      stage('Build') {
            steps {
               sh 'mvn install -DskipTests'
            }
            post {
               success {
                     echo 'Now Archiving it...'
                     archiveArtifacts artifacts: '**/target/*.war'
               }
            }
      }
   }
}
```

## 💾 10. Handle Jenkins Disk Space Issue

**Problem:** Jenkins builds and plugins can fill up disk space, causing *No space left on device* errors.

### Steps to Fix:

1. **Check disk space**:
    ```bash
    df -h
    fdisk -l
    ```
2. **Increase volume size in EC2** (e.g., from 8GB to 20GB+):
    - EC2 Dashboard → Elastic Block Store → Volumes  
    - Modify Volume → Set Size → 20GB → Save

3. **Reboot Jenkins EC2 instance**:
    - EC2 → Actions → Reboot

4. **Verify new size after reboot**:
    ```bash
    df -h
    fdisk -l
    ```

---

## ✅ Summary

- ✅ Launched secure Jenkins EC2  
- ✅ Installed Jenkins with JDK & Maven  
- ✅ Set up toolchains in Jenkins  
- ✅ Created CI jobs for source checkout, build, test, archive  
- ✅ Integrated with Nexus & SonarQube  
- ✅ Resolved disk space issues  
- ✅ Added Declarative Pipeline for automation

---