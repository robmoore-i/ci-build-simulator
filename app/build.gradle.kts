plugins {
    idea
    groovy
    application
    id("ci.build.simulator.generateTests")
    id("ci.build.simulator.jenkins")
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
        implementation("org.apache.commons:commons-text:1.9")
        implementation("org.codehaus.groovy:groovy-all:3.0.7")
        implementation("com.offbytwo.jenkins:jenkins-client:0.3.8") // https://github.com/jenkinsci/java-client-api
        testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    }
    implementation("org.codehaus.groovy:groovy-all")
    implementation("com.offbytwo.jenkins:jenkins-client")
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Required for old javax dependencies used within the Jenkins client library
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")
}

tasks.test {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
    }
}
