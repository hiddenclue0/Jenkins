# 📦 Nexus Repository Manager Setup on AWS EC2

This guide explains how I deployed **Nexus Repository Manager** on an **AWS EC2 instance** running **Amazon Linux 2023**. It includes EC2 setup, security group configuration, and automated installation using a custom shell script.

---

## ☁️ 1. Launch EC2 Instance

I launched a new EC2 instance with the following configuration:

- **Name**: `Nexus Server`
- **AMI**: Amazon Linux 2023
- **Instance Type**: `t2.medium` (or `t3.medium` for better performance)
- **Key Pair**: Created a new key pair named `Nexus-key.pem`
- **Security Group**: Created a group called `nexus-sg` with these inbound rules:

| Port  | Protocol | Source                | Purpose                     |
|-------|----------|------------------------|-----------------------------|
| 22    | TCP      | My IP Address          | SSH access                  |
| 8081  | TCP      | My IP Address          | Nexus web UI                |
| 8081  | TCP      | Jenkins Security Group | Allow Jenkins to upload artifacts |

In the directory I added my `nexus-setup.sh` script to automate Nexus installation.

---

## 🔧 2. Access Nexus Server

Once the EC2 instance was running, I accessed Nexus with the steps below:

1. SSH into the instance:
   ```bash
   ssh -i Nexus-key.pem ec2-user@<your-ec2-public-ip>
   ```

2. Switch to the root user and check the Nexus service:
   ```bash
   sudo -i
   systemctl status nexus
   ```

3. Open the Nexus web interface in your browser:
   ```
   http://<your-ec2-public-ip>:8081
   ```

4. Retrieve the initial admin password:
   ```bash
   cat /opt/nexus/sonatype-work/nexus3/admin.password
   ```

5. Login using:
   - **Username**: `admin`
   - **Password**: (from the file above)

6. Set a new password (e.g., `admin123`) and enable **anonymous access** so users or Jenkins can download artifacts without logging in.

---

## 🧾 3. Notes

- I made sure VPNs or proxies were disabled while accessing the web UI to avoid connection issues.
- Nexus will serve as a private artifact repository for my Jenkins CI/CD pipeline.
- Credentials like `admin/admin123` were stored securely for integration with Jenkins plugins.

---

## 🗂️ 4. Directory and Tools

- Nexus was installed at: `/opt/nexus`
- Java 17 (Amazon Corretto) was installed automatically using the script below.

---

## 📜 5. `nexus-setup.sh` Script

Below is the full installation script used to automate the Nexus setup. This can be reused as EC2  or executed manually.

```bash
#!/bin/bash

sudo rpm --import https://yum.corretto.aws/corretto.key
sudo curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo

sudo yum install -y java-17-amazon-corretto-devel wget -y

mkdir -p /opt/nexus/
mkdir -p /tmp/nexus/
cd /tmp/nexus/
NEXUSURL="https://download.sonatype.com/nexus/3/nexus-unix-x86-64-3.78.0-14.tar.gz"
wget $NEXUSURL -O nexus.tar.gz
sleep 10
EXTOUT=`tar xzvf nexus.tar.gz`
NEXUSDIR=`echo $EXTOUT | cut -d '/' -f1`
sleep 5
rm -rf /tmp/nexus/nexus.tar.gz
cp -r /tmp/nexus/* /opt/nexus/
sleep 5
useradd nexus
chown -R nexus.nexus /opt/nexus
cat <<EOT>> /etc/systemd/system/nexus.service
[Unit]
Description=nexus service
After=network.target

[Service]
Type=forking
LimitNOFILE=65536
ExecStart=/opt/nexus/$NEXUSDIR/bin/nexus start
ExecStop=/opt/nexus/$NEXUSDIR/bin/nexus stop
User=nexus
Restart=on-abort

[Install]
WantedBy=multi-user.target

EOT

echo 'run_as_user="nexus"' > /opt/nexus/$NEXUSDIR/bin/nexus.rc
systemctl daemon-reload
systemctl start nexus
systemctl enable nexus
```

---

## ✅ Summary

- Secure EC2 instance provisioning with proper access controls.
- Automated installation of Java and Nexus using a custom script.
- Ready-to-use private repository accessible from Jenkins and developers.

---