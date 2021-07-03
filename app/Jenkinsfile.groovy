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
        sh("./gradlew :app:generateStableTest")
    }
}