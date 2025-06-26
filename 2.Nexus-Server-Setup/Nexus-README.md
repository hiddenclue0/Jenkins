# 📦 Nexus Repository Manager Setup on AWS EC2

This document describes how I deployed **Nexus Repository Manager** on an EC2 instance running Amazon Linux 2023, set up security groups, and automated the installation using a shell script.

---

## 🚀 Launching the EC2 Instance

To begin, I launched a new EC2 instance with the following configuration:

- **Name**: Nexus Server  
- **AMI**: Amazon Linux 2023  
- **Instance Type**: t2.medium (or t3.medium for better performance)  
- **Key Pair**: I created a new key pair and named it `Nexus-key`  
- **Security Group**: I created a new group called `nexus-sg` with these rules:
  - Port **22**: SSH access allowed from my IP
  - Port **8081**: HTTP access from my IP
  - Port **8081**: Also allowed access from **Jenkins Security Group**, since Jenkins needs to upload artifacts to Nexus

In the **Advanced Details** section during instance launch, I added the content of my `nexus-setup.sh` script into the **User Data** field to automate the Nexus installation and setup.

---

## 🔧 Accessing Nexus

After the instance was up and running, I accessed Nexus as follows:

1. I SSHed into the instance using:
   ```bash
   ssh -i Nexus-key.pem ec2-user@<Public-IP>
   ```

2. Then I switched to the root user and checked if the Nexus service was running:
   ```bash
   sudo su
   systemctl status nexus
   ```

3. In my browser, I opened the Nexus dashboard at:  
   `http://<Public-IP>:8081`

4. To log in for the first time, I fetched the initial admin password:
   ```bash
   cat /opt/sonatype-work/nexus3/admin.password
   ```

5. I logged in with:
   - Username: `admin`
   - Password: (copied from the file)

6. Then, I changed the default password to something easier to remember (e.g., `admin123`) and enabled **anonymous access** so that artifacts could be downloaded without logging in.

---

## ✅ Notes

- I made sure VPNs or browser proxies were disabled while accessing the Nexus web interface to avoid connectivity issues.
- This Nexus instance will be used to store build artifacts uploaded from Jenkins jobs.
- The credentials (like `admin/admin123`) were saved securely for use with Jenkins Nexus plugins.

---

## 📁 Home Directory & Tools

- Nexus was installed in: `/opt/nexus`
- Java 17 was installed automatically via the `nexus-setup.sh` script