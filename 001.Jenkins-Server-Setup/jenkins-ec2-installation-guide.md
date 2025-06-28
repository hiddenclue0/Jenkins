# 🚀 Jenkins CI/CD Setup on AWS EC2 (Ubuntu/Debian)

This guide provides a complete walkthrough for setting up Jenkins on an AWS EC2 instance with security best practices, disk management, and full CI/CD pipeline integration using tools like Maven, SonarQube, and Nexus.

---

## ☁️ 1. EC2 Instance Setup for Jenkins

**Instance Configuration:**

| Property         | Value                  |
|------------------|------------------------|
| Name             | Jenkins Server         |
| AMI              | Ubuntu 24.04 LTS       |
| Instance Type    | t2.medium or t3.medium |
| Key Pair         | Jenkins-server-KP.pem  |
| Security Group   | jenkins-sg             |

**Security Group Inbound Rules (jenkins-sg):**

| Port | Protocol | Source        | Purpose          |
|------|----------|---------------|------------------|
| 22   | TCP      | My IP only    | SSH access       |
| 8080 | TCP      | My IP only    | Jenkins Web UI   |
| 8080 | TCP      | SonarQube SG  | Webhook Callback |

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
sudo apt install openjdk-17-jdk -y
sudo apt install openjdk-21-jdk -y

sudo wget -O /usr/share/keyrings/jenkins-keyring.asc https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt-get update
sudo apt-get install jenkins -y
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

---

## 🔌 6. Install Plugins

Install **Suggested Plugins** during setup, then install the following via **Manage Jenkins > Plugins**:
- Maven Integration
- GitHub Integration
- Pipeline
- SonarQube Scanner
- Nexus Artifact Uploader

---

## 🛠️ 7. Manage Jenkins > Global Tool Configuration

Under **Manage Jenkins > Global Tool Configuration**:

- Add JDK 17, 21  
- Add Maven 3.9

Example:
```text
JDK:
  Name: JDK17
  Path: /usr/lib/jvm/java-17-openjdk-amd64

Maven:
  Name: MAVEN3.9
  Auto install or provide binary path
```

---

## 💾 8. Handle Jenkins Disk Space Issue

**Problem:** Jenkins builds and plugins consume storage. You might encounter *No space left on device*.

### Steps to Fix:

1. **Check disk space**:
   ```bash
   df -h
   fdisk -l
   ```
2. **Modify volume size in EC2** (from 8GB to 20GB+):
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

## 🔄 9. Flow of Jenkins CI/CD Pipeline

1. Checkout source code from GitHub  
2. Build project using Maven  
3. Run test cases  
4. Analyze code with SonarQube  
5. Archive artifacts (.war)  
6. Upload artifacts to Nexus  

---

## 🔁 10. Steps for Continuous Integration Pipeline

1. **Create Freestyle Job** `vprofile-build`  
   - Set GitHub repo: `https://github.com/hiddenclue0/vprofileApp-jenkins-cicd-automation.git`  
   - Branch: `main`  

2. **Select JDK 17 and Maven 3.9**

3. **Build Step**: Use "Invoke Top-Level Maven Targets"  
   - Maven Version: MAVEN3.9  
   - Goals: `clean install`

4. **Post-Build Actions**:  
   - Archive artifact: `**/*.war`

5. **New Job (Optional)**: `vprofile-test`  
   - Copy from `vprofile-build`  
   - Change Maven Goal: `test`  
   - Test different JDKs and Maven versions

---

## 🔐 11. Additional Security Groups

### 📦 Nexus Security Group (nexus-sg)

| Port | Protocol | Source                | Purpose         |
|------|----------|------------------------|-----------------|
| 22   | TCP      | My IP Address          | SSH             |
| 8081 | TCP      | My IP Address          | Nexus UI        |
| 8081 | TCP      | Jenkins Security Group | Artifact Upload |

### 🧪 SonarQube Security Group (sonar-sg)

| Port | Protocol | Source                | Purpose           |
|------|----------|------------------------|-------------------|
| 22   | TCP      | My IP Address          | SSH               |
| 9000 | TCP      | My IP Address          | Sonar UI          |
| 9000 | TCP      | Jenkins Security Group | Push Code Analysis |

---

## 📂 12. Recommended Repository Structure

```
Jenkins/
├── 1.Installation/
│   ├── jenkins-setup.sh
│   └── jenkins-ec2-installation-guide.md
├── 2.Security-Groups/
│   └── jenkins-nexus-sonarqube-sg.md
├── 3.Pipeline-Jobs/
│   └── vprofile-ci-job.md
```

---

## 🧾 13. Jenkins Pipeline Code (Declarative)

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

---

## ✅ Summary

- ✅ Launched secure Jenkins EC2  
- ✅ Installed Jenkins with JDK & Maven  
- ✅ Set up toolchains under Jenkins management  
- ✅ Created Jenkins CI jobs with source code checkout, build, test, archive  
- ✅ Enabled secure communication with Nexus & SonarQube  
- ✅ Mitigated disk space errors with volume resize  
- ✅ Added Declarative Pipeline for automation
