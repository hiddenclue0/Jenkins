# 🚀 vProfileApp CI/CD Pipeline with Slack Notification

This project automates the CI/CD process of the `vProfileApp` Java web application using **Jenkins**, integrated with **Slack**, **SonarQube**, and **Docker**.

---

## 📦 Project Structure

This Jenkins pipeline performs the following steps:

### 🔄 Stages in the Pipeline

1. **Fetch Code**
   - Pulls code from the GitHub repository (`docker` branch).

2. **Build**
   - Builds the Java project using Maven.
   - Archives the `.war` artifacts for further usage.

3. **Unit Test**
   - Runs unit tests using Maven.

4. **Checkstyle Analysis**
   - Performs code style checks using Maven Checkstyle plugin.

5. **SonarQube Code Analysis**
   - Analyzes code using SonarQube (configured with `sonar6.2` tool and `sonarserver` instance).

6. **Quality Gate**
   - Waits for SonarQube quality gate results and aborts pipeline if the gate fails.

7. **Build App Docker Image**
   - Builds a Docker image using a multistage Dockerfile.

8. **Upload Docker Image**
   - Pushes the image to AWS ECR with both versioned (`:$BUILD_NUMBER`) and `latest` tags.

9. **Slack Notification (Post Actions)**
   - Sends a Slack message after the pipeline is completed, indicating success or failure with color-coded status.

---

## 🔔 Slack Integration

Slack notifications are configured in the `post` section of the pipeline. Steps to integrate Slack with Jenkins:

1. Install the **Slack Notification Plugin** from Jenkins.
2. Create a **Slack Workspace** and **channel** (e.g., `#devopscicd`).
3. Go to the [Slack App Directory](https://api.slack.com/apps) and add the **Jenkins CI** integration.
4. Copy the generated token.
5. In Jenkins:
   - Navigate to **Manage Jenkins > Configure System**.
   - Set Slack workspace and default channel.
   - Add a **Secret Text credential** with the token (e.g., ID: `slacktoken`).

Slack notification is configured like this:

```groovy
post {
    always {
        echo 'Slack Notification'
        slackSend(channel: '#devopscicd',
                  color: COLOR_MAP(currentBuild.currentResult),
                  message: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' - ${currentBuild.currentResult}")
    }
}

def COLOR_MAP(String result) {
    if (result == 'SUCCESS') return 'good'
    if (result == 'FAILURE') return 'danger'
    return 'warning'
}
```

---

## 🛠 Tools Used

- **Jenkins**
- **Maven 3.9**
- **Java JDK 17**
- **SonarQube**
- **Docker**
- **AWS ECR**
- **Slack**

---

## ✅ Prerequisites

- Jenkins running with required tools and plugins.
- SonarQube configured with `sonarserver` and `sonar6.2` scanner.
- AWS ECR credentials stored in Jenkins as `ecr:us-east-2:awscreds`.
- Slack workspace and token properly configured.

---

## 🧪 Testing Slack Notifications

To test failure notifications, you can add a stage that runs an invalid command:

```groovy
stage('Test Slack Failure') {
    steps {
        sh 'NotARealCommand'
    }
}
```

You’ll receive a red-colored failure message in your Slack channel.

---

## 📌 Author

**hkhcoder** — DevOps Enthusiast | Jenkins Automation | Docker | AWS | SonarQube | CI/CD

---

## 📷 Screenshot

Slack Notifications will look like this:
- ✅ Green = Success
- ❌ Red = Failure

---

## 📄 License

This project is open-source and available for educational and testing purposes.
