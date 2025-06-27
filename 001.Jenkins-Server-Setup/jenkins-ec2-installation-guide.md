# 🚀 Jenkins Installation on AWS EC2 (Ubuntu/Debian)

This guide provides step-by-step instructions to deploy **Jenkins** on an **AWS EC2 instance** running Ubuntu or Debian.

---

## 1️⃣ Launch an EC2 Instance

Ensure your **security group** allows access from your IP to:

- **SSH** (port **22**)
- **Jenkins Web UI** (port **8080**)

### 🖥️ Launch via AWS CLI

```bash
aws ec2 run-instances \
  --image-id ami-020cba7c55df1f615 \
  --instance-type t2.micro \
  --key-name Jenkins-server-KP \
  --security-group "edit inound rule | port 22 from myip | port 8080 from myip"
```

> 🔐 Replace `sg-preview-1` with your security group ID that allows TCP 22 and 8080 from your IP.

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
