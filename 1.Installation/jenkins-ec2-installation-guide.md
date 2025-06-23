
# 🚀 Jenkins Installation on AWS EC2 (Ubuntu/Debian)

This guide provides step-by-step instructions to deploy **Jenkins** on an **AWS EC2 instance** running Ubuntu or Debian.

---

## 1️⃣ Launch an EC2 Instance

### a. Configure Security Group

Allow inbound access for SSH (port 22) and Jenkins Web UI (port 8080):

```bash
# Allow SSH (Port 22)
aws ec2 authorize-security-group-ingress \
  --group-id sg-preview-1 \
  --protocol tcp --port 22 --cidr 103.197.153.50/32

# Allow Jenkins Web UI (Port 8080)
aws ec2 authorize-security-group-ingress \
  --group-id sg-preview-1 \
  --protocol tcp --port 8080 --cidr 103.197.153.50/32
```

### b. Launch the EC2 Instance

```bash
aws ec2 run-instances \
  --image-id ami-020cba7c55df1f615 \
  --instance-type t2.micro \
  --key-name Jenkins-server-KP \
  --security-group-ids sg-preview-1 \
  --count 1 \
  --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=Jenkins-Server}]'
```

---

## 2️⃣ Install Java (Required for Jenkins)

SSH into your EC2 instance and install Java:

```bash
# Update package index
sudo apt-get update

# Install OpenJDK 11 (recommended)
sudo apt-get install openjdk-11-jdk -y

# Verify Java installation
java -version
```

---

## 3️⃣ Install Jenkins

Still on your EC2 instance, run:

```bash
# Download Jenkins GPG key
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

# Add Jenkins repository
echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" \
  | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null

# Update and install Jenkins
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

1. Find your EC2 public IP from the AWS Console or CLI.
2. Open your browser and go to:  
   `http://<your-public-ip>:8080`
3. Retrieve the initial admin password:
   ```bash
   sudo cat /var/lib/jenkins/secrets/initialAdminPassword
   ```

---

## 📁 Recommended GitHub Repository Structure

```
jenkins-on-ec2/
├── README.md
├── screenshots/
│   └── <optional images>
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

