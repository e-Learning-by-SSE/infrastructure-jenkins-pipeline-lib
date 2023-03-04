pipeline {
  agent any
  
  options {
    ansiColor('xterm')
  }

  stages {
    stage('Syntax Check') {
      agent {
        docker {
          image 'groovy:latest'
        }
      }    
      steps {
          sh 'groovyc -cp vars vars/*.groovy'
      }
    }
  }
}

