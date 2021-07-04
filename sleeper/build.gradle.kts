plugins {
    idea
    groovy
    application
    id("ci.build.simulator.generateTests")
    id("ci.build.simulator.jenkins")
}

application {
    mainClass.set("ci.build.simulator.sleeper.App")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    constraints {
        implementation("org.codehaus.groovy:groovy-all:3.0.7")
        testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    }
    implementation("org.codehaus.groovy:groovy-all")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
    }
}
