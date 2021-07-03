pipeline {
    agent any
    environment {
        GITHUB_CREDENTIALS = credentials('GitHubPushAccess')
    }
    stages {
        stage("Run tests") {
            steps {
                sh("./gradlew :app:test")
            }
        }
        stage("Extend test suite") {
            steps {
                sh("./gradlew :app:generateStableTest")
                sh("git commit -am'Extended test suite'")
                echo("Git URL is $GIT_URL")
                echo("Credentials: $GITHUB_CREDENTIALS")
            }
        }
    }
}