package ci.build.simulator.jenkins

import org.gradle.api.Project

object CreateJobTaskInputValidator {
    fun validateTaskInputsFrom(project: Project) {
        if (!(project.hasProperty("branch") &&
                    project.hasProperty("url") &&
                    project.hasProperty("user") &&
                    project.hasProperty("password"))
        ) {
            throw RuntimeException(
                "Missing project properties. Example usage:\n" +
                        "./gradlew :app:createJob -Pbranch=main -Purl=http://13.229.56.106:8080 -Puser=jenkins -Ppassword=secret"
            )
        }
    }
}