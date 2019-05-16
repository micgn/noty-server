def EMAIL_TO = 'spa1@trali.de'


pipeline {
    options {
      buildDiscarder(logRotator(numToKeepStr: '3', artifactNumToKeepStr: '3'))
      disableConcurrentBuilds()
    }
    triggers {
        pollSCM('H */2 * * *')
    }
    agent {
        docker {
            image 'micgn/build:1.1'
            args '-v $HOME/.m2:/root/.m2:z -v /var/run/docker.sock:/var/run/docker.sock -u root --net="host"'
            reuseNode true
        }
    }
    stages {
        stage('build & test') {
            steps {
                sh 'mvn -B clean install'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('dockerize') {
            when { branch 'master' }
            steps {
                // ***************  ADAPT ***************
                sh "docker build -t micgn/noty-server:1.0 ."
            }
        }
    }
    post {
        failure {
            emailext body: 'Check console output at $BUILD_URL to view the results. \n\n ${CHANGES} \n\n -------------------------------------------------- \n${BUILD_LOG, maxLines=100, escapeHtml=false}',
                    to: EMAIL_TO,
                    subject: 'Build failed in Jenkins: $PROJECT_NAME - #$BUILD_NUMBER'
        }
        unstable {
            emailext body: 'Check console output at $BUILD_URL to view the results. \n\n ${CHANGES} \n\n -------------------------------------------------- \n${BUILD_LOG, maxLines=100, escapeHtml=false}',
                    to: EMAIL_TO,
                    subject: 'Unstable build in Jenkins: $PROJECT_NAME - #$BUILD_NUMBER'
        }
        changed {
            emailext body: 'Check console output at $BUILD_URL to view the results.',
                    to: EMAIL_TO,
                    subject: 'Jenkins build is back to normal: $PROJECT_NAME - #$BUILD_NUMBER'
        }
    }
}
