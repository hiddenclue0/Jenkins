# 🛠️ Jenkins Pipeline Instructions (CI/CD for vProfile Project)

This README explains how I set up a **Jenkins Pipeline as code** for my `vprofile-project`. It includes a breakdown of concepts, how I implemented the pipeline, and the Jenkinsfile used in the job.

---

## 📘 What is Pipeline as Code?

**Pipeline as code** in Jenkins means writing your CI/CD process in a file called `Jenkinsfile` (with a capital "J"). This file defines the various **stages** in the build lifecycle (like fetching code, testing, building, archiving) using a special syntax called **declarative pipeline**, which is now the standard.

---

## 🧱 Jenkinsfile Structure Overview

The Jenkinsfile is structured with:

- `pipeline`: the root block
- `agent`: defines where to run the pipeline
- `tools`: mentions globally configured tools like Maven and JDK
- `environment`: (optional) for variables
- `stages`: multiple steps such as fetching code, testing, and building
- `steps`: commands or plugin invocations
- `post`: actions triggered after stage success/failure (e.g., archiving)

---

## 🧪 My Pipeline Stages

I created three stages:

1. **Fetch Code**: Uses the Git plugin to pull the code from GitHub.
2. **Unit Test**: Runs tests using `mvn test`.
3. **Build**: Builds the `.war` file with `mvn install -DskipTests` and archives it.

---

## 🧾 Jenkinsfile

Here is the complete Jenkinsfile I used:

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
                git branch: 'main', url: 'https://github.com/hiddenclue0/vprofileApp-jenkins-cicd-automation.git'
            }
        }

        stage('UNIT TEST') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn install -DskipTests'
            }
            post {
                success {
                    echo 'Now Archiving it...'
                    archiveArtifacts artifacts: '**/target/*.war'
                }
            }
        }
    }
}
```

---

## ⚙️ Creating the Pipeline Job

Here’s how I created and tested this pipeline in Jenkins:

1. Open Jenkins Dashboard → **New Item**
2. Name it `VProfile Pipeline`
3. Select **Pipeline** type → click **OK**
4. Scroll to the **Pipeline** section
5. Either:
   - Paste the above Jenkinsfile in the **Pipeline Script** section
   - OR choose **Pipeline Script from SCM** to fetch from GitHub
6. Save and click **Build Now**

---

## 🖥️ Viewing Output

- Each stage execution can be viewed separately in Jenkins
- Click on stage names to see individual console logs
- Final `.war` file will be archived on success

---

## 🔍 Troubleshooting Tips

- Use **Console Output** tab for detailed logs
- Ensure Maven, JDK, Git plugins are properly configured
- Archive pattern (`**/target/*.war`) must match your output location

---

## 💡 Final Notes

- You can build more complex pipelines by extending this template with SonarQube analysis, artifact versioning, Docker builds, etc.
- Refer to plugin documentation or ChatGPT for syntax help
- Once familiar, writing Jenkinsfile becomes second nature!

---