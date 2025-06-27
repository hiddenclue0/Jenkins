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

## 🔜 Coming Up

In the next phase, I’ll:
- Update the Jenkinsfile to include Nexus upload logic
- Use the `nexuslogin` credentials ID in the pipeline
- Trigger builds and confirm the artifact is available in the Nexus UI

Stay tuned!
