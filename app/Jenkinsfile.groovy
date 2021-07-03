pipeline {
    agent any

    stages {
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