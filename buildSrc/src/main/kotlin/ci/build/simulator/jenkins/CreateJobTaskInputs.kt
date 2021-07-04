package ci.build.simulator.jenkins

import com.offbytwo.jenkins.JenkinsServer
import org.gradle.api.Project
import java.net.URI

data class CreateJobTaskInputs(val branch: String, val url: String, val user: String, val password: String) {

    val jobName = branch.replace('/', '-')

    companion object {
        fun usingPropertiesFromProject(project: Project): CreateJobTaskInputs {
            if (!(project.hasProperty("branch") &&
                        project.hasProperty("url") &&
                        project.hasProperty("user") &&
                        project.hasProperty("password"))
            ) {
                throw RuntimeException(
                    "Missing project properties. Example usage:\n" +
                            "./gradlew :app:createJob -Pbranch=simulation/1 -Purl=http://13.229.56.106:8080 -Puser=jenkins -Ppassword=secret"
                )
            }

            if (!(project.property("branch") as String).startsWith("simulation/")) {
                throw RuntimeException("Use a branch name that starts with 'simulation/'.\n" +
                        "For example, 'simulation/test' or 'simulation/8'")
            }

            return CreateJobTaskInputs(
                project.property("branch") as String,
                project.property("url") as String,
                project.property("user") as String,
                project.property("password") as String
            )
        }
    }

    fun getJenkinsServer(): JenkinsServer = JenkinsServer(URI(url), user, password)
}