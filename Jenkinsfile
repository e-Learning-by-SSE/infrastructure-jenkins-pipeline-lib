pipeline {
    agent { label 'maven' }
    
    options {
        ansiColor('xterm')
    }

    stages {

        stage('Git') {
            steps {
                cleanWs()
                git branch: 'main', url: 'https://github.com/e-Learning-by-SSE/nm-api-generator.git'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x *.sh'
                withMaven(mavenSettingsConfig: 'mvn-elearn-repo-settings') {
                    sh './generator.sh'
                }
            }
        }
    }

    post {
        always {
             // Send e-mails if build becomes unstable/fails or returns stable
             // Based on: https://stackoverflow.com/a/39178479
             load "$JENKINS_HOME/.envvars/emails.groovy"
             step([$class: 'Mailer', recipients: "${env.elsharkawy}", notifyEveryUnstableBuild: true, sendToIndividuals: false])

             // Store generated Zip archives
             archiveArtifacts artifacts: '*.zip', onlyIfSuccessful: true
        }
    }
}
