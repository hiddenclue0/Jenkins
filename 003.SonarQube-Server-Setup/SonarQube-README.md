# 📊 SonarQube Server Setup on AWS EC2

This guide explains how I deployed **SonarQube** on an **AWS EC2 Ubuntu instance** using **NGINX** as a reverse proxy and integrated it with Jenkins for automated code analysis.

---

## ☁️ 1. Launch EC2 Instance

I launched a new EC2 instance with the following configuration:

- **Name**: `Sonar Server`
- **AMI**: Ubuntu 24.04 LTS (Free Tier Eligible)
- **Instance Type**: `t2.medium` (SonarQube requires at least 4 GB RAM)
- **Key Pair**: Created a new key pair named `Sonar-key.pem`
- **Security Group**: Created a group called `sonar-sg` with the following inbound rules:

| Port | Protocol | Source                  | Purpose                          |
|------|----------|--------------------------|----------------------------------|
| 22   | TCP      | My IP Address            | SSH Access                       |
| 80   | TCP      | My IP Address            | NGINX Proxy to SonarQube (Web UI)|
| 80   | TCP      | Jenkins Security Group   | Allow Jenkins to push results    |

During launch, I pasted the contents of my `sonar-setup.sh` script into the directory for full automation.

---

## 🔧 2. Access SonarQube Server

After the instance finished booting and setup completed, I accessed the SonarQube web interface:

1. SSH into the EC2 instance:
   ```bash
   ssh -i Sonar-key.pem ubuntu@<your-ec2-public-ip>
   ```

2. Open your browser and visit:
   ```
   http://<your-ec2-public-ip>
   ```

3. Login using default credentials:
   - **Username**: `admin`
   - **Password**: `admin`

4. Change the admin password on first login (e.g., `admin123`).

---

## 🔁 3. Jenkins ↔ SonarQube Integration

To enable Jenkins to send code quality reports to SonarQube:

- **Jenkins → SonarQube**: Already allowed by allowing **port 80** from Jenkins security group.
- **SonarQube → Jenkins** (optional for callbacks): I added an inbound rule in the **Jenkins Security Group**:

| Port | Protocol | Source         | Purpose                      |
|------|----------|----------------|------------------------------|
| 8080 | TCP      | `sonar-sg`     | Allow SonarQube to reach Jenkins |

---

## 📋 4. Script and Services Info

- Java 17 (OpenJDK) was installed.
- PostgreSQL is used as the backend database.
- NGINX reverse proxies SonarQube on port 80.
- SonarQube service runs under a dedicated `sonar` user.
- Data stored in `/opt/sonarqube` and PostgreSQL.

---

## 📜 5. `sonar-setup.sh` Script

The entire SonarQube setup was automated using the script below. This script was added to EC2's user data at launch time.

```bash
#!/bin/bash
cp /etc/sysctl.conf /root/sysctl.conf_backup
cat <<EOT> /etc/sysctl.conf
vm.max_map_count=262144
fs.file-max=65536
ulimit -n 65536
ulimit -u 4096
EOT

cp /etc/security/limits.conf /root/sec_limit.conf_backup
cat <<EOT> /etc/security/limits.conf
sonarqube   -   nofile   65536
sonarqube   -   nproc    409
EOT

sudo apt-get update -y
sudo apt-get install openjdk-17-jdk -y

sudo apt update
wget -q https://www.postgresql.org/media/keys/ACCC4CF8.asc -O - | sudo apt-key add -
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'
sudo apt install postgresql postgresql-contrib -y
sudo systemctl enable postgresql.service
sudo systemctl start postgresql.service
sudo echo "postgres:admin123" | chpasswd
runuser -l postgres -c "createuser sonar"
sudo -i -u postgres psql -c "ALTER USER sonar WITH ENCRYPTED PASSWORD 'admin123';"
sudo -i -u postgres psql -c "CREATE DATABASE sonarqube OWNER sonar;"
sudo -i -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE sonarqube to sonar;"
systemctl restart postgresql

sudo mkdir -p /sonarqube/
cd /sonarqube/
sudo curl -O https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-9.9.8.100196.zip
sudo apt-get install zip -y
sudo unzip -o sonarqube-9.9.8.100196.zip -d /opt/
sudo mv /opt/sonarqube-9.9.8.100196/ /opt/sonarqube
sudo groupadd sonar
sudo useradd -c "SonarQube - User" -d /opt/sonarqube/ -g sonar sonar
sudo chown sonar:sonar /opt/sonarqube/ -R

cp /opt/sonarqube/conf/sonar.properties /root/sonar.properties_backup
cat <<EOT> /opt/sonarqube/conf/sonar.properties
sonar.jdbc.username=sonar
sonar.jdbc.password=admin123
sonar.jdbc.url=jdbc:postgresql://localhost/sonarqube
sonar.web.host=0.0.0.0
sonar.web.port=9000
sonar.web.javaAdditionalOpts=-server
sonar.search.javaOpts=-Xmx512m -Xms512m -XX:+HeapDumpOnOutOfMemoryError
sonar.log.level=INFO
sonar.path.logs=logs
EOT

cat <<EOT> /etc/systemd/system/sonarqube.service
[Unit]
Description=SonarQube service
After=syslog.target network.target

[Service]
Type=forking
ExecStart=/opt/sonarqube/bin/linux-x86-64/sonar.sh start
ExecStop=/opt/sonarqube/bin/linux-x86-64/sonar.sh stop
User=sonar
Group=sonar
Restart=always
LimitNOFILE=65536
LimitNPROC=4096

[Install]
WantedBy=multi-user.target
EOT

systemctl daemon-reload
systemctl enable sonarqube.service

apt-get install nginx -y
rm -rf /etc/nginx/sites-enabled/default
rm -rf /etc/nginx/sites-available/default

cat <<EOT> /etc/nginx/sites-available/sonarqube
server {
    listen      80;
    server_name sonarqube.groophy.in;

    access_log  /var/log/nginx/sonar.access.log;
    error_log   /var/log/nginx/sonar.error.log;

    proxy_buffers 16 64k;
    proxy_buffer_size 128k;

    location / {
        proxy_pass  http://127.0.0.1:9000;
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
        proxy_redirect off;

        proxy_set_header    Host            \$host;
        proxy_set_header    X-Real-IP       \$remote_addr;
        proxy_set_header    X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header    X-Forwarded-Proto http;
    }
}
EOT

ln -s /etc/nginx/sites-available/sonarqube /etc/nginx/sites-enabled/sonarqube
systemctl enable nginx.service

sudo ufw allow 80,9000,9001/tcp

echo "System reboot in 30 sec"
sleep 30
reboot

```

---

## ✅ Summary

- Deployed SonarQube with PostgreSQL and NGINX reverse proxy on EC2 Ubuntu.
- Automated setup using a detailed `sonar-setup.sh` script.
- Integrated Jenkins with SonarQube via secure security group rules.
- Enabled full visibility into code quality metrics via the SonarQube dashboard.
---
