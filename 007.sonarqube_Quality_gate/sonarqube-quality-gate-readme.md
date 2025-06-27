# ✅ SonarQube Quality Gate Integration with Jenkins CI

This guide documents how to integrate **SonarQube Quality Gates** with a Jenkins CI pipeline for the `vprofile` project. Quality gates help enforce code quality thresholds (e.g., bugs, vulnerabilities) before passing the build.

---

## 🔧 Quality Gate Setup in SonarQube

### 1. Create Custom Quality Gate
- Navigate to **Quality Gates** in SonarQube UI.
- Click **Create**, name it `vprofile-QG`.
- Unlock editing mode and click **Add Condition**.
  - Example:  
    - Metric: `Bugs`
    - Condition: `is greater than`
    - Threshold: `10`
- Save the Quality Gate.

### 2. Apply Quality Gate to Project
- Navigate to your project (e.g., `vprofile`) > **Project Settings** > **Quality Gate**.
- Switch from the default to the custom gate `vprofile-QG`.
- Save the changes.

---

## 🌐 Create Webhook for Jenkins Notification

To notify Jenkins of the quality gate result:

### 1. Setup Webhook
- Go to your SonarQube project > **Project Settings** > **Webhooks**.
- Click **Create Webhook**.
- Name: `Jenkins-ci-webhook`
- URL Format:  
  ```
  http://<jenkins-private-ip>:8080/sonarqube-webhook
  ```
  Example: `http://10.0.1.10:8080/sonarqube-webhook`  
  (Replace with your Jenkins private IP)
- Save the webhook.

---

## 📦 Jenkins Pipeline Integration

### 1. Add Quality Gate Stage in Jenkinsfile
- After the SonarQube analysis stage, add:
```groovy
stage('Quality Gate') {
    steps {
        timeout(time: 1, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}
```
> This step causes the pipeline to **wait** for SonarQube to evaluate the quality gate and **fail** if conditions are not met.

### 2. Update Jenkinsfile
- Insert this stage **after** the SonarQube code analysis stage.
- Ensure proper `{}` closures.

---

## 🔐 Security Group Configuration

Ensure Jenkins can receive webhook from SonarQube:

- Jenkins EC2 security group must allow **port 8080** inbound **from the SonarQube EC2**.
- Rule example:
  - Type: `Custom TCP`
  - Port: `8080`
  - Source: `SonarQube SG` (Security Group ID)

---

## ✅ Expected Behavior

- If project violates the quality gate (e.g., >10 bugs), pipeline fails.
- SonarQube UI and Jenkins logs will indicate:
  ```
  ERROR: Quality Gate failed: ERROR
  ```

---

## 🔄 Resetting Quality Gate (Optional)

To continue builds without gate enforcement:
- Go to project settings > **Quality Gate**
- Switch back to **default** (`Sonar way`) or increase thresholds (e.g., bugs > 50)

---

## 🧪 Test and Verify

1. Run the Jenkins job.
2. After code analysis, check the `Quality Gate` stage:
   - Should fail if conditions violated.
   - Should pass if code meets the threshold.

---

## 📎 Reference

- Jenkins SonarQube Plugin Docs
- SonarQube Quality Gate Webhook Documentation
- Your vprofile Jenkins CI/CD Project

---
