node {
    stage("Checkout") {
        sh("git --version")
        checkout scm
        sh("ls")
    }
    stage("Run tests") {
        sh("./gradlew :app:test")
    }
    stage("Extend test suite") {
        def gitBranch = "main"
        sh("git checkout $gitBranch")
        sh("git clean -fd")
        sh("./gradlew :app:generateStableTest")
        echo("Git URL is $GIT_URL")
        sh("git config --global user.email \"jenkins@email.com\"")
        sh("git config --global user.name \"Jenkins\"")
        sh("git add app")
        sh("git commit -am \"Extended test suite\"")
        withCredentials([usernameColonPassword(credentialsId: 'GitHubPushAccess', variable: 'GITHUB_CREDENTIALS')]) {
            def truncatedGitUrl = "$GIT_URL".drop("https://".length())
            sh("git push https://${GITHUB_CREDENTIALS}@$truncatedGitUrl")
        }
    }
}