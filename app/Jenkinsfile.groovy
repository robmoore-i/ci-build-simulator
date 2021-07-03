node {
    environment {
        GITHUB_CREDENTIALS = credentials('GitHubPushAccess')
    }
    stage("Run tests") {
        sh("./gradlew :app:test")
    }
    stage("Extend test suite") {
        def BRANCH = "$GIT_BRANCH".drop("origin/".length())
        sh("git checkout $BRANCH")
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