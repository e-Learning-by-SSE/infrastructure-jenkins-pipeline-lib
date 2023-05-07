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
                assert version == "1.2" : "Wrong maven version, expected 1.2 got ${version}"
              }
            }
            
            echo 'test stagingDeploy'
            stagingDeploy("echo ok")

            echo 'test packageJson.isNewVersion'              
            dir('tests/npm/temp') {
              script {
                def commitFile = { name -> 
                  sh "touch ${name}"
                  sh "git add ${name}"
                  sh "git commit -m \"test ${name}\""
                }
                sh 'git init'
                sh 'git config user.email "jenkins@jenkins"'
                sh 'git config user.name "jenkins"'
                
                commitFile("README")
                commitFile("ANOTHER_README")
                assert packageJson.isNewVersion(since: 'PREVIOUS_REVISION') == false

                sh 'cp ../package.json ./'
                sh 'git add package.json'
                sh 'git commit -m "test commit"'
                assert packageJson.isNewVersion(since: 'PREVIOUS_REVISION') == true

                assert packageJson.getVersion() == '1.0.1'
              }
            }
          }
        }

         stage('Test') {
            steps {
                script {
  
                }
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
                publishNpm('e-learning-by-sse')
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

        stage('NPM Version Expression Test') {
          when {
            expression {
              return packageJson.isNewVersion
            }
          }
          steps {
            // this must be triggered and checked manually - no automation possible so far
            // triggered when a commit is made which changes any package.json version inside this repository
            echo 'package.json was changed'
          }
        }
      }
    }
  }
}
