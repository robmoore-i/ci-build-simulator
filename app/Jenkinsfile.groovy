node {
    stage("Setup") {
        sh("ls")
        sh("git --version")
        sh("git branch")
        sh("git branch | grep \"*\"")
        sh("git branch | grep \"*\" | awk '{print \$2}'")
        String gitBranch = "main"
        sh("git checkout $gitBranch")
        sh("git reset --hard origin/$gitBranch")
        sh("git pull")
        sh("git clean -fd")
        sh("git config --global user.email \"robmoore121+Jenkins@gmail.com\"")
        sh("git config --global user.name \"Jenkins Moore\"")
    }
    stage("Run tests") {
        sh("./gradlew :app:test")
    }
    if (false) {
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
}