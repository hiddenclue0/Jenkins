# 🚀 Jenkins Pipeline: Uploading Artifacts to Nexus Repository

This document explains how I implemented artifact versioning and uploading to a Nexus Repository through a Jenkins Pipeline.

---

## 📋 Jenkinsfile Pipeline Overview

In my CI pipeline for the `vprofile` project, I defined the following stages:

1. **Fetch Code** – Clones the repo from GitHub
2. **Build** – Builds the project and archives the `.war` file
3. **Unit Tests** – Executes unit tests
4. **Checkstyle Analysis** – Performs static code analysis
5. **Sonar Code Analysis** – Runs SonarQube scanning
6. **Quality Gate** – Enforces SonarQube quality standards
7. **Upload Artifact** – Pushes the final `.war` file to Nexus Repository

---

## 💡 Nexus Artifact Uploader Plugin

I used the `nexusArtifactUploader` plugin in Jenkins to upload artifacts to Nexus 3.

### 🧾 Required Jenkins Credentials

Before configuring the pipeline:
- I created **username/password** credentials in Jenkins with:
  - **ID**: `nexuslogin`
  - **Username**: `admin`
  - **Password**: `admin123`

> ⚠️ These credentials must match the credentials for Nexus server access.

---

## 🏗️ Versioning Artifacts

To ensure each artifact is uniquely versioned:
- I used the Jenkins environment variables:  
  `${BUILD_ID}-${BUILD_TIMESTAMP}`  
- To enable `BUILD_TIMESTAMP`, I configured **Global Properties**:
  - Enabled **Build Timestamp Plugin**
  - Set format: `yyMMdd_HH-mm`

---

## 📄 Upload Stage in Jenkinsfile

Here’s the relevant stage from my pipeline:

```groovy
stage("UploadArtifact") {
  steps {
    nexusArtifactUploader(
      nexusVersion: 'nexus3',
      protocol: 'http',
      nexusUrl: '172.31.25.14:8081',
      groupId: 'QA',
      version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
      repository: 'vprofile-repo',
      credentialsId: 'nexuslogin',
      artifacts: [
        [artifactId: 'vproapp',
         classifier: '',
         file: 'target/vprofile-v2.war',
         type: 'war']
      ]
    )
  }
}
```

> ✅ Make sure the IP and file path match your Nexus server and project structure.

---

## 🛠 Troubleshooting Build Failures

- ❌ **Disk Space Error**: Jenkins master node ran out of space.  
  ✅ Solution: I logged into the Jenkins host and ran:
  ```bash
  sudo rm -rf /var/lib/jenkins/workspace/*
  sudo systemctl restart jenkins
  ```

- ❌ **Wrong Nexus IP**: Artifact upload failed due to incorrect private IP.  
  ✅ Solution: I corrected the IP in `nexusUrl`.

---

## 📦 Verifying Artifacts in Nexus

To confirm the upload:
1. I logged into Nexus: `http://<nexus-ip>:8081`
2. Browsed to `vprofile-repo`
3. Found multiple artifact versions like:
   ```
   vproapp/23-06-27_19-45/vproapp-23-06-27_19-45.war
   ```

Each successful pipeline run now pushes a uniquely versioned `.war` file to Nexus.

---

## 🔔 What's Next?

In the next step, I plan to add notifications (Slack/email) for pipeline status updates.

