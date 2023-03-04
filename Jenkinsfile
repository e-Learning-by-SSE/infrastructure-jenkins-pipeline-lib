pipeline {
  agent { label 'docker' }

  options {
    ansiColor('xterm')
  }

  stages {
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
    stage('Installation Tests') {
      steps {
        script {
          def libraryName = 'web-service-helper-lib'
          def library = library(libraryName)
          def classLoader = library.getClass().getClassLoader()
          def groovyFiles = findFiles(glob: '**/*.groovy', excludes: 'resources/**', type: 'file')

          groovyFiles.each {
            file -> 
              try {
                classLoader.parseClass(file.text)
                println "Syntax check passed for ${file.path}"
              } catch (Throwable t) {
                println "Syntax check failed for ${file.path}: ${t.message}"
              }
          }
        }
      }
    }

  }
}