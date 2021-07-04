node {
    stage("Setup") {
        sh("ls")
        String gitBranch = "main"
        sh("git checkout $gitBranch")
        sh("git pull")
    }
    stage("Run tests") {
        sh("./gradlew :app:test")
    }
    stage("Extend test suite") {
        sh("git clean -fd")
        sh("./gradlew :app:generateStableTest")
        sh("git config --global user.email \"jenkins@email.com\"")
        sh("git config --global user.name \"Jenkins\"")
        sh("git add app")
        sh("git commit -am \"Extended test suite\"")
        withCredentials([usernameColonPassword(credentialsId: 'GitHubPushAccess', variable: 'GITHUB_CREDENTIALS')]) {
            String gitUrl = sh(returnStdout: true, script: 'git config remote.origin.url').trim()
            String truncatedGitUrl = gitUrl.drop("https://".length())
            sh("git push https://${GITHUB_CREDENTIALS}@$truncatedGitUrl")
        }
    }
}