node {
    stage("Setup") {
        boolean alreadyCheckedOut = sh(returnStatus: true, script: "ls .git") == 0
        if (!alreadyCheckedOut) {
            checkout scm
        }
        sh("pwd")
        sh("ls")
        sh("git --version")
        sh("echo $JAVA_HOME")
        sh("java -version")
        sh("javac -version")
        sh("git branch")
        echo("$JOB_NAME")
        String branch = "$JOB_NAME".split("_")[1].replace('-', '/')
        echo("branch = $branch")
        sh("git checkout $branch")
        sh("git reset --hard origin/$branch")
        sh("git pull")
        sh("git clean -fd")
        sh("git config --global user.email \"robmoore121+jenkins@gmail.com\"")
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
    String branch = "$JOB_NAME".split("_")[1].replace('-', '/')
    def numberOfCommits = sh(returnStdout: true, script: "git rev-list --count $branch ^origin/main").trim().toInteger()
    def maxNumberOfCommitsInSimulation = 4
    echo("Number of commits in this simulation so far: $numberOfCommits. Will stop at $maxNumberOfCommitsInSimulation")
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