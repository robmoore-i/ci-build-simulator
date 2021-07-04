node {
    stage("Setup") {
        sh("ls")
        sh("git --version")
        String gitBranch = "main"
        sh("git checkout $gitBranch")
        sh("git pull")
        sh("git reset --hard origin/$gitBranch")
        sh("git clean -fd")
        sh("git config --global user.email \"robmoore121+Jenkins@gmail.com\"")
        sh("git config --global user.name \"Jenkins Moore\"")
    }
    stage("Run tests") {
        sh("./gradlew :app:test")
    }
    stage("Extend test suite") {
        sh("./gradlew :app:extendTestSuite")
        sh("git add app")
        sh("git commit -am \"(Jenkins) Extended test suite\"")
        String gitUrl = sh(returnStdout: true, script: "git config remote.origin.url").trim()
        String truncatedGitUrl = gitUrl.drop("https://".length())
        withCredentials([usernameColonPassword(credentialsId: "GitHubPushAccess", variable: "GITHUB_CREDENTIALS")]) {
            sh("git push https://${GITHUB_CREDENTIALS}@$truncatedGitUrl")
        }
    }
}