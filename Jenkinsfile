pipeline {
  agent any
  stages {
    stage('Prepare environment') {
      steps {
        milestone(ordinal: 1, label: 'Prepare pipeline environment')
      }
    }
    stage('Build Criters') {
      steps {
        milestone(ordinal: 2, label: 'Build and test code')
        dir(path: 'criters-reactor') {
          sh 'mvn verify'
        }
        
      }
    }
  }
  post {
    always {
      deleteDir()
      
    }
    
    success {
      mail(to: 'daniel.sundberg@oyabun.se', subject: "Jenkins build ${currentBuild.fullDisplayName} succeeded.", body: 'Yay, we passed.')
      
    }
    
    failure {
      mail(to: 'daniel.sundberg@oyabun.se', subject: "Jenkins build ${currentBuild.fullDisplayName} failed.", body: 'Boo, we failed.')
      
    }
    
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
  }
}