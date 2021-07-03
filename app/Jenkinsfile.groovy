node {
    println("Running Jenkins pipeline")
    sh("ls")
    sh("git --version")
    checkout scm
    sh("ls")
}