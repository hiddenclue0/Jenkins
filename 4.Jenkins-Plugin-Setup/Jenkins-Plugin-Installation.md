# 🔌 Jenkins Plugin Installation for CI/CD Setup

In this guide, I walk through the steps I followed to install essential Jenkins plugins that integrate with Nexus, SonarQube, Git, and support advanced pipeline features for my CI/CD automation project.

---

## ✅ Pre-requisites

Before installing plugins, I completed the setup of:
- Jenkins
- Nexus Repository Manager
- SonarQube Server

I also ensured that the necessary ports were open to the public in the EC2 Security Groups:
- Jenkins: **8080**
- Nexus: **8081**
- SonarQube: **80** and **9000**

---

## 🔧 Installing Plugins in Jenkins

I navigated to **Manage Jenkins > Manage Plugins > Available** tab and installed the following plugins:

### 1. Nexus Artifact Uploader
- 🔍 **Search**: `Nexus Artifact Uploader`
- ✅ Used for uploading build artifacts (like `.jar` files) to the Nexus repository from Jenkins.

### 2. SonarQube Scanner
- 🔍 **Search**: `SonarQube Scanner`
- ✅ Enables integration of Jenkins with SonarQube for code analysis.

### 3. Git Plugin
- ✅ Usually comes pre-installed.
- ✅ Required to pull source code from GitHub repositories in Jenkins jobs.

### 4. Pipeline Maven Integration
- 🔍 **Search**: `Pipeline Maven Integration`
- ✅ Allows Maven-based projects to be built within pipeline scripts.

### 5. Build Timestamp
- 🔍 **Search**: `Build Timestamp`
- ✅ Useful for adding timestamp-based versioning to build artifacts.

### 6. Pipeline Utility Steps
- 🔍 **Search**: `Pipeline Utility Steps`
- ✅ Adds helper functions that can be used in Jenkins pipelines (e.g., file manipulation, JSON handling).

---

## 🚀 How I Installed

1. I opened **Jenkins Dashboard**.
2. Went to **Manage Jenkins > Manage Plugins**.
3. Switched to the **Available** tab.
4. Used the search bar to find each plugin listed above.
5. Checked the box next to each and clicked **Install without restart**.

---

## 📝 Notes

- Some plugins may require dependencies that Jenkins will install automatically.
- After installation, I made sure to restart Jenkins for all plugin features to be fully available.
- More plugins will be installed later depending on pipeline requirements.

This setup ensures Jenkins is ready for building, testing, and deploying Java-based applications using Maven, Nexus, and SonarQube.