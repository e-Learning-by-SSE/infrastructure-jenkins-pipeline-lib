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
  }
}