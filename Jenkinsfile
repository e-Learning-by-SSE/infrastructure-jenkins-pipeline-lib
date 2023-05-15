@Library('github.com/e-Learning-by-SSE/nm-jenkins-groovy-helper-lib@main') _

pipeline {
  agent {
    label 'docker'
  }

  options {
    ansiColor('xterm')
  }

  stages {

    stage('SCM') {
      steps {
        cleanWs()
        checkout scmGit(
          branches: [[name: 'main']],
          userRemoteConfigs: [[url: 'https://github.com/e-Learning-by-SSE/nm-jenkins-groovy-helper-lib.git']])
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
      failFast false
      steps {
        //sh '''
        //  groovyc -cp /opt/groovy/lib/*:vars:src:/core/jenkins-core*.jar:$(find /plugins -name \'*.jar\' -printf \'%p:\') vars/*.groovy
        //  '''
        echo 'Syntax ok - checked with groovy compiler'
      }
    }

    stage('Start Function Tests') {
      parallel {

        stage('Misc Function Tests') {
          steps {     
            
            echo 'test scriptOut'
            script {
              assert scriptOut('echo "hi"') == "hi"
            }
            
            echo 'test getMvnProjectVersion'
            dir('tests/maven') {
              script {
                assert maven.getProjectVersion() == "1.2.0" : "Wrong maven version, expected 1.2.0 got ${version}"
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
                  sh "git commit -m \"commit ${name}\" "
                }
                sh 'git init'
                sh 'git config user.email "jenkins@jenkins"'
                sh 'git config user.name "jenkins"'
                
                commitFile("README")
                commitFile("ANOTHER_README")
                assert packageJson.isNewVersion(since: 'PREVIOUS_REVISION') == false, "There was no version change in th last revision"

                sh 'cp ../package.json ./'
                sh 'git add package.json'
                sh 'git commit -m "test commit"'
                assert packageJson.isNewVersion(since: 'PREVIOUS_REVISION') == true, "There was an undetected version change in the last revision"

                echo 'test packageJson.getVersion'
                assert packageJson.getVersion() == '1.0.0'
                
                deleteDir()
              }
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
                npmPublish('e-learning-by-sse')
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

        stage('Docker build and publish Test') {
          steps {
            script {
              ssedocker {
                buildImage {
                  dockerfile 'tests/docker'
                  target 'ghcr.io/e-learning-by-sse/test-docker-image:latest'
                }
                publish {
                  additionalTag 'test'
                  additionalTag 'test2'
                }
                sh 'docker image rm ghcr.io/e-learning-by-sse/test-docker-image:test'
                // github doesn't allow to delete a package per cli. so in order to test this, the package 'ghcr.io/e-learning-by-sse/test-docker-image:latest' must be deleted in beforehand
                new net.ssehub.customdocker.SSEDocker(docker).withRegistry {
                  docker.image('ghcr.io/e-learning-by-sse/test-docker-image:test').pull()
                }
              }
            }
          }
        }
      }
    }
  }
}
