pipeline 
{
	agent any
	tools 
	{
		maven "MAVEN3.9"
		jdk "JDK17"
	}
	stages 
	{
		stage('Fetch code'){
			steps{
				git branch: 'main', url: 'https://github.com/hiddenclue0/vprofileApp-jenkins-cicd-automation.git'
			}
		}
		stage('UNIT TEST') {
			steps{
				sh 'mvn test'
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
	}
}