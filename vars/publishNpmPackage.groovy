def call(path, token, registry='//npm.pkg.github.com/') {
    pipeline {
        agent {
            docker {
                image 'node:18-bullseye'
                label 'docker'
                reuseNode true
                args '--tmpfs /.cache -v $HOME/.npm:/.npm'
            }
        }
        stages {
            stage('NPM Publish') {
                steps {
                    dir("$path") {
                        sh 'npm i'
                        sh 'rm -f ~/.npmrc'
                        sh "echo $registry:_authToken=$token >> ~/.npmrc"
                        sh "npm publish --access public --registry=https:${registry}"
                        sh 'rm -f ~/.npmrc'
                    }
                }
            }
        }
    }
}
