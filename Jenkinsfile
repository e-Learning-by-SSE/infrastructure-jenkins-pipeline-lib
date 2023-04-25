pipeline {
  agent {
    label 'docker'
  }

  options {
    ansiColor('xterm')
  }

  stages {

    // Tests if the groovy syntax is ok
    stage('Syntax Test') {
      agent {
        docker {
          image 'groovy:latest'
        }
      }
      steps {
        sh 'groovyc -cp vars vars/*.groovy'
      }
    }
    stage('Function Tests') {
      agent {
        label 'maven'
      }
      steps {      
        library 'github.com/e-Learning-by-SSE/nm-jenkins-groovy-helper-lib@main'
        
        dir('tests/maven') {
          script {
            def version = getMvnProjectVersion()
            assert version == "1.0.0" : "Wrong maven version, expected 1.0.0 got ${version}"
          }
        }
        
        stagingDeploy("echo ok")
      }
    }
    
    stage('Postgres Sidecar Test') {
       environment {
         // passed down to js file
         DB_USER = 'myuser'
         DB_HOST = 'localhost'
         DB_NAME = 'mydatabase'
         DB_PASSWORD = 'mypassword'
         DB_PORT = '5432'
      }
      steps {
        dir('tests/postgresSidecar') {
          postgresSidecar('node:latest', "${env.DB_USER}", "${env.DB_PASSWORD}", "${env.DB_NAME}", "${env.DB_PORT}", dockerArgs: "--tmpfs /.cache -v $HOME/.npm:/.npm") {
            sh 'npm install pg'
            sh 'node run testconnection.js'
          }
        }
      }
    }
  }
}
