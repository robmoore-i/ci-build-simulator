plugins {
    `kotlin-dsl`
    idea
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.codehaus.groovy:groovy-all:3.0.7")
}

gradlePlugin {
    plugins {
        create("testGenerator") {
            id = "ci.build.simulator.generateTests"
            implementationClass = "ci.build.simulator.generatetests.GenerateTestsPlugin"
        }

        create("jenkins") {
            id = "ci.build.simulator.jenkins"
            implementationClass = "ci.build.simulator.jenkins.JenkinsPlugin"
        }
    }
}