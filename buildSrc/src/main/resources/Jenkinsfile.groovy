node {
    stage("Setup") {
        boolean alreadyCheckedOut = sh(returnStatus: true, script: "ls .git") == 0
        if (!alreadyCheckedOut) {
            checkout scm
        }
        sh("pwd")
        sh("ls")
        sh("git --version")
        sh("git branch")
        echo("$JOB_NAME")
        String gitBranch = "$JOB_NAME".split("_")[1].replace('-', '/')
        echo("gitBranch = $gitBranch")
        sh("git checkout $gitBranch")
        sh("git reset --hard origin/$gitBranch")
        sh("git pull")
        sh("git clean -fd")
        sh("git config --global user.email \"robmoore121+Jenkins@gmail.com\"")
        sh("git config --global user.name \"Jenkins\"")
    }
    String projectName = "$JOB_NAME".split("_")[0]
    echo("projectName = $projectName")
    stage("Run tests") {
        sh("./gradlew :$projectName:test")
    }
    stage("Simulate development") {
        sh("./gradlew :$projectName:simulateDevelopment")
        sh("git add .")
        sh("git commit -am \"(Jenkins) Extended test suite\"")
        String gitUrl = sh(returnStdout: true, script: "git config remote.origin.url").trim()
        String truncatedGitUrl = gitUrl.drop("https://".length())
        withCredentials([usernameColonPassword(credentialsId: "GitHubPushAccess", variable: "GITHUB_CREDENTIALS")]) {
            sh("git push https://${GITHUB_CREDENTIALS}@$truncatedGitUrl")
        }
    }
    def numberOfCommits = sh(returnStdout: true, script: "git rev-list --count HEAD").trim().toInteger()
    def maxNumberOfCommitsInSimulation = 80
    echo("Number of commits so far: $numberOfCommits. Will stop at $maxNumberOfCommitsInSimulation")
    stage("Check simulation") {
        if (numberOfCommits < maxNumberOfCommitsInSimulation) {
            build job: "$JOB_NAME", wait: false
        }
    }
    if (numberOfCommits >= maxNumberOfCommitsInSimulation) {
        stage("Finish simulation") {
            echo("That's all folks")
        }
    }
}