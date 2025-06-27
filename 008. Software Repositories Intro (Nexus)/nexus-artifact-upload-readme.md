# 📦 Uploading Artifacts to Nexus Repository from Jenkins

I documented here how I configured my Jenkins pipeline to upload artifacts (build outputs) to a **Nexus Repository Manager**. This setup allows me to version and store application artifacts, making them available for future deployments or automation workflows.

---

## 🔍 Why Use Nexus Repository?

When I run `mvn install`, Maven downloads dependencies from the internet. However, I can configure it to download dependencies from my own Nexus repository.

My goal in this step is to store the **vprofile project** artifact into my Nexus repository. Later, I can also use this repository as a **private package manager** for various formats like:

- `maven` - Java dependencies
- `apt` - Debian packages
- `yum` - RPM packages
- `npm` - NodeJS modules
- `docker` - Container images

---

## 🚀 Setting Up Nexus Repository

I had already set up my **Nexus Sonatype Repository** (which runs on Java) using an EC2 user-data script. Here's how I accessed and configured it:

1. I retrieved the public IP of the Nexus server.
2. Accessed it at: `http://<public-ip>:8081`
3. Logged in using the admin credentials I set earlier (admin/admin123).
4. Clicked on the **gear icon** to access the settings.
5. Went to **Repositories** → Clicked **Create repository**.
6. Chose **maven2 (hosted)** since I wanted to *store* artifacts.
   - If I wanted to *proxy* remote repositories, I’d choose `maven2 (proxy)`.
   - Groups are for combining hosted + proxy into one.
7. Named my new repository `vprofile-repo` and clicked **Create repository**.

---

## 🔐 Setting Jenkins Credentials for Nexus

Before integrating Jenkins with Nexus, I needed to set up credentials in Jenkins.

### Steps I followed:

1. Navigated to **Jenkins Dashboard** → **Manage Jenkins** → **Credentials**.
2. Went into **(global)** domain → Clicked **Add Credentials**.
3. Chose:
   - **Kind**: `Username with password`
   - **Username**: `admin`
   - **Password**: `admin123`
   - **ID**: `nexuslogin`
   - **Description**: `nexuslogin`
4. Saved the credentials.

> I made sure to remember the credential ID `nexuslogin`, since I used it in my pipeline script.

---

## 🧰 Summary

By completing this setup:
- I created a private Maven repository called `vprofile-repo`.
- I configured Jenkins to authenticate with Nexus using saved credentials.
- I laid the foundation to upload and version artifacts via my Jenkins CI/CD pipeline.

In the next step, I’ll write the pipeline code that will build my project and push the artifact into the Nexus repository.

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


