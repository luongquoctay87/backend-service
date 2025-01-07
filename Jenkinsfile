pipeline {
    agent any

    tools {
      maven 'MAVEN'
      jdk 'JDK17'
    }

    stages {
        stage('Clean workspace') {
            steps {
               cleanWs()
            }
        }

        stage("Checkout from SCM"){
            steps {
              git branch: 'init-project-base', credentialsId: 'github', url: 'git@github.com:luongquoctay87/backend-service'
            }
        }

        stage("Test Application"){
            steps {
                script {
                    sh "mvn -v"
                    sh "mvn test -P test"
                }
            }
        }

        stage("Build Application"){
            steps {
                script {
                    sh "mvn clean package -P test"
                }
            }
        }

        stage("Deploy Application"){
            steps {
                script {
                    sh 'docker --version'

                    // docker stop
                    sh 'docker stop $(docker ps -aq)'

                    //remove
                    sh 'docker rm $(docker ps -aq)'

                    // build new image
                    sh 'docker build -t backend-service .'

                    // docker list
                    sh 'docker images'

                    // run docker
                    sh 'docker run -d -p 8080:8080 backend-service:latest'
                }
            }
        }
    }
}
