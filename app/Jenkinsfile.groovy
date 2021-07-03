pipeline {
    agent any

    stages {
        stage("Checkout") {
            steps {
                sh("git --version")
                checkout scm
                sh("ls")
            }
        }
        stage("Run tests") {
            steps {
                sh("./gradlew :app:test")
            }
        }
        stage("Extend test suite") {
            steps {
                sh("./gradlew :app:generateStableTest")
            }
        }
    }
}