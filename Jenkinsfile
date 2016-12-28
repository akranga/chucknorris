node("java") {
  stage('Build') { 
    sh "pwd" 
    sh "ls -al" 
    sh "./gradlew jar" 
    archiveArtifacts artifacts: 'build/libs/chnorr-*.jar', fingerprint: true 
  }

  stage('Test') {
    sh "./gradlew test"
    junit 'build/test-results/*.xml'
  }
}


node {
  stage('Dockerize') { 
    echo "creating a docker conainer"
  }
}


node {
  stage('Push') { 
    echo "Push a docker to registry"
  }
}


node {
  stage('Schedule') { 
    echo "Run docker container as part of kubernetes"
  }
}