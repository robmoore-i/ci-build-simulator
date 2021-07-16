import ci.build.simulator.simulate.development.simulators.SleeperDevelopmentSimulator

plugins {
    idea
    groovy
    application
    id("ci.build.simulator.simulate.development")
    id("ci.build.simulator.jenkins")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
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

simulator {
    instance.set(SleeperDevelopmentSimulator(logger))
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
    }
}
