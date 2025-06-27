
# 📊 Jenkins CI Pipeline with Checkstyle and SonarQube Integration

In this project, I implemented a Jenkins pipeline for my `vprofile-project` that automates the CI process. It performs code checkout, builds the project, runs unit tests, performs static code analysis using **Checkstyle**, and pushes the results to **SonarQube** for full code quality inspection.

---

## 🚀 Project Overview

This pipeline is designed to:

- Automate the build and test process.
- Analyze code style violations with **Checkstyle**.
- Upload Checkstyle and other test reports to **SonarQube** for visibility into code quality, test coverage, and maintainability.

---

## 🔄 CI Pipeline Stages

### 🧬 1. Fetch Code
```groovy
git branch: 'atom', url: 'https://github.com/hkhcoder/vprofile-project.git'
```

### 🛠️ 2. Build
```bash
mvn install -DskipTests
```
Archives the generated WAR:
```groovy
archiveArtifacts artifacts: '**/target/*.war'
```

### 🧪 3. Unit Testing
```bash
mvn test
```

### 🧹 4. Checkstyle Analysis
```bash
mvn checkstyle:checkstyle
```
Generates `target/checkstyle-result.xml`.

### 📈 5. Code Coverage (Jacoco)
```bash
mvn jacoco:prepare-agent test jacoco:report
```
Generates `target/site/jacoco/jacoco.xml`.

### 🔍 6. SonarQube Code Analysis
```
withSonarQubeEnv('sonarserver') {
    sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=vprofile        -Dsonar.projectName=vprofile        -Dsonar.projectVersion=1.0        -Dsonar.sources=src/        -Dsonar.java.binaries=target/test-classes/com/visualpathit/account/controllerTest/        -Dsonar.junit.reportsPath=target/surefire-reports/        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml        -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
}
```

### ✅ 7. Quality Gate (Optional)
```groovy
stage("Quality Gate") {
    steps {
        timeout(time: 1, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}
```

---

## 🧾 Full Jenkins Pipeline Code

```groovy
pipeline {
	agent any
	tools {
	    maven "MAVEN3.9"
	    jdk "JDK17"
	}

	stages {

	    stage('Fetch code') {
            steps {
               git branch: 'atom', url: 'https://github.com/hkhcoder/vprofile-project.git'
            }
	    }

	    stage('Build'){
	        steps{
	           sh 'mvn install -DskipTests'
	        }
	        post {
	           success {
	              echo 'Now Archiving it...'
	              archiveArtifacts artifacts: '**/target/*.war'
	           }
	        }
	    }

	    stage('UNIT TEST') {
            steps{
                sh 'mvn test'
            }
        }

        stage('Checkstyle Analysis') {
            steps{
                sh 'mvn checkstyle:checkstyle'
            }
        }

        stage("Sonar Code Analysis") {
        	environment {
                scannerHome = tool 'sonar6.2'
            }
            steps {
              withSonarQubeEnv('sonarserver') {
                sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=vprofile \\
                   -Dsonar.projectName=vprofile \\
                   -Dsonar.projectVersion=1.0 \\
                   -Dsonar.sources=src/ \\
                   -Dsonar.java.binaries=target/test-classes/com/visualpathit/account/controllerTest/ \\
                   -Dsonar.junit.reportsPath=target/surefire-reports/ \\
                   -Dsonar.jacoco.reportsPath=target/jacoco.exec \\
                   -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
              }
            }
        }
	}
}

```
---

## 🤖 How I Write and Troubleshoot Pipeline Code

1. I search for relevant Jenkinsfile and SonarQube integration examples on **Google**.
2. I use **ChatGPT** to convert my ideas into working pipeline code.
3. I explore official **SonarQube Docs** and **Jenkins Plugin Pages**.
4. I test incrementally and fix errors based on **console output logs**.

---

## 📊 CI/CD Workflow Diagram

```
    A[Code Commit] --> B[Jenkins CI Trigger]
    B --> C[Build & Unit Test]
    C --> D[Checkstyle]
    D --> E[Code Coverage (Jacoco)]
    E --> F[SonarQube Analysis]
    F --> G[Reports Published]
```

---

## ✅ Final Result

- Build artifacts archived
- Unit tests run and reports generated
- Checkstyle XML output created
- Jacoco XML coverage report generated
- SonarScanner uploaded results to SonarQube

---

> 💡 **Tip**: Make sure paths are correct for reports and sources; otherwise, SonarQube may miss the data.
