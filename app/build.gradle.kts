plugins {
    idea
    groovy
    application
    id("ci.build.simulator.generateTests")
}

application {
    mainClass.set("ci.build.simulator.app.App")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    constraints {
        // Define dependency versions as constraints
        implementation("org.apache.commons:commons-text:1.9")

        implementation("org.codehaus.groovy:groovy-all:3.0.7")
    }

    // Use the latest Groovy version for building this library
    implementation("org.codehaus.groovy:groovy-all")

    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}

tasks.test {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
    }
}
