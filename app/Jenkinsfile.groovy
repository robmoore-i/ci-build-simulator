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
                def branch = "$GIT_BRANCH".drop("origin/".length())
                sh("git checkout $branch")
                sh("git clean -fd")
                sh("./gradlew :app:generateStableTest")
                echo("Git URL is $GIT_URL")
                echo("Credentials: $GITHUB_CREDENTIALS")
                sh("git config --global user.email \"jenkins@email.com\"")
                sh("git config --global user.name \"Jenkins\"")
                sh("git config --global github.user $GITHUB_CREDENTIALS_USR")
                sh("git config --global github.token $GITHUB_CREDENTIALS_PSW")
                sh("git add app")
                sh("git commit -am \"Extended test suite\"")
                sh("git push")
            }
        }
    }
}