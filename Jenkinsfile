@Library('github.com/e-Learning-by-SSE/nm-jenkins-groovy-helper-lib@main') _

pipeline {
  agent {
    label 'docker'
  }

  options {
    ansiColor('xterm')
    skipDefaultCheckout true
  }

  stages {

    stage('SCM') {
      steps {
        cleanWs()
        checkout scm
      } 
    }

    stage('Vars Syntax Test') {
      agent {
        docker {
          image 'groovy:latest'
          label 'master'
          args '-v /var/lib/jenkins/plugins/:/plugins -v /var/cache/jenkins/war/WEB-INF/lib/:/core'
        }
      }
      steps {
        sh '''
          set +x
          groovyc -cp /opt/groovy/lib/*:vars:src:/core/jenkins-core-*.jar:$(find /plugins -name \'*.jar\' -printf \'%p:\') vars/*.groovy
          set -x
          '''
        echo 'Syntax ok - checked with groovy compiler'
      }
    }

    stage('Start Function Tests') {
      parallel {

        stage('Misc Function Tests') {
          agent {
            label 'docker && maven'
          }
          steps {     

            echo 'test getMvnProjectVersion'
            dir('tests/maven') {
              script {
                def version = getMvnProjectVersion()
                assert version == "1.0.0" : "Wrong maven version, expected 1.0.0 got ${version}"
              }
            }
            
            echo 'test stagingDeploy'
            stagingDeploy("echo ok")

          }
        }
          
        stage ('NPM Publish Test') {
          agent {
            docker {
              image 'node'
              reuseNode true
              label 'docker'
              args '--tmpfs /.cache -v $HOME/.npm:/.npm'
            }
          }
          steps {
            dir('tests/npm') {
                publishNpmIfNotExist('@e-learning-by-sse', 'ci_dummy_test', '1.0.0', 'e-learning-by-sse')
            }
          }
        }

        stage('With Postgres Test') {
          environment {
            DB_PORT = 5433
          }
          steps {
            dir('tests/postgresSidecar') {
              withPostgres([dbExposedPort: env.DB_PORT]) {
                script {
                  def exitCode = sh(script: "nc -zv localhost ${env.DB_PORT}", returnStatus: true)
                  assert exitCode == 0, "The test script failed with exit code ${exitCode}, port is not reachable"               
                }
              }
            }
          }
        }

        stage('Postgres double Sidecar Test') {
          environment {
            // passed down to js file
            DB_USER = 'myuser'
            DB_HOST = 'db' // is resolved inside a docker image
            DB_NAME = 'mydatabase'
            DB_PASSWORD = 'mypassword'
            DB_PORT = '5432'
          }
          steps {
            dir('tests/postgresSidecar') {
              script {
                withPostgres([ dbUser: DB_USER,  dbPassword: DB_PASSWORD,  dbName: DB_NAME]).insideSidecar("node:latest", "--tmpfs /.cache -v $HOME/.npm:/.npm") {
                  sh 'npm install pg'
                  def exitCode = sh(script: 'node testconnection.js', returnStatus: true)
                  assert exitCode == 0, "The test script failed with exit code ${exitCode}"
                }
              }
            }
          }
        }
      }
    }
  }
}
