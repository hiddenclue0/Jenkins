# 🚀 Jenkins Installation on AWS EC2 (Ubuntu/Debian)

This guide provides step-by-step instructions to deploy **Jenkins** on an **AWS EC2 instance** running Ubuntu or Debian.

---

## 🚀 Launching the EC2 Instance

To begin, I launched a new EC2 instance with the following configuration:

- **Name**: Jenkins Server  
- **AMI**: Ubuntu Server 24.04 LTS  
- **Instance Type**: t2.medium (or t3.medium for better performance)  
- **Key Pair**: I created a new key pair and named it `Jenkins-key`  
- **Security Group**: I created a new group called `Jenkins-sg` with these rules:
  - Port **22**: SSH access allowed from my IP
  - Port **8080**: HTTP access from my IP

In the same directory I added the content of my `jenkins-setup.sh` script to automate the Jenkins installation and setup.

---

## 2️⃣ Install Java (Required for Jenkins)

SSH into your EC2 instance:

```bash
ssh -i "Jenkins-server-KP.pem" ubuntu@<your-ec2-public-ip>
```

Install Java:

```bash
# Update package index
sudo apt-get update

# Upgrade packages
sudo apt-get upgrade -y

# Install OpenJDK 21
sudo apt-get install openjdk-21-jdk -y

# Verify Java installation
java -version
```

---

## 3️⃣ Install Jenkins

Run the following commands on your EC2 instance:

```bash
# Download Jenkins GPG key
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

# Add Jenkins repository
echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" \
  | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null

# Update package index and install Jenkins
sudo apt-get update
sudo apt-get install jenkins -y
```

---

## 4️⃣ Start and Enable Jenkins

```bash
# Start Jenkins
sudo systemctl start jenkins

# Enable Jenkins to start on boot
sudo systemctl enable jenkins

# Check Jenkins status
sudo systemctl status jenkins
```

---

## 5️⃣ Access Jenkins

1. Find your EC2 **public IP** from the AWS Console or CLI.
2. Open your browser and go to:

   ```
   http://<your-public-ip>:8080
   ```

3. Retrieve the initial admin password:

   ```bash
   sudo cat /var/lib/jenkins/secrets/initialAdminPassword
   ```

4. Complete setup with the following credentials:

   - **Install suggested plugins**
   - **Username:** `jenkins`
   - **Password:** `jenkins123`
   - **Confirm password:** `jenkins123`
   - **Full name:** `jenkins`
   - **E-mail address:** `jenkins@gmail.com`
   - **Jenkins URL:** `http://jenkins:8080/`

---

## 📁 Recommended GitHub Repository Structure

```
Jenkins/
└── 1.Installation/
    ├── Jenkins-server-KP.pem
    └── jenkins-ec2-installation-guide.md
```

---

## ✅ Useful Commands

```bash
# Start Jenkins
sudo systemctl start jenkins

# Enable Jenkins on boot
sudo systemctl enable jenkins

# Check Jenkins status
sudo systemctl status jenkins
```

---

## 📝 Summary of Fixes/Changes:
- Fixed incorrect `--security-group-ids` syntax (was a comment instead of a value).
- Added escaping to multiline commands.
- Added placeholder `<your-ec2-public-ip>` for better clarity.
- Improved formatting for repository structure.
- Added clarification notes for security group usage.
