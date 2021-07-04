node {
    stage("Setup") {
        boolean alreadyCheckedOut = sh(returnStatus: true, script: "ls .git") == 0
        if (!alreadyCheckedOut) {
            checkout scm
        }
        sh("pwd")
        sh("ls")
        sh("git --version")
        String gitBranch = sh(returnStdout: true, script: "git branch | grep \"^*\" | awk '{print \$2}'").trim()
        sh("git checkout $gitBranch")
        sh("git reset --hard origin/$gitBranch")
        sh("git pull")
        sh("git clean -fd")
        sh("git config --global user.email \"robmoore121+Jenkins@gmail.com\"")
        sh("git config --global user.name \"Jenkins\"")
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