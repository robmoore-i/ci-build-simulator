package ci.build.simulator.jenkins

import com.offbytwo.jenkins.JenkinsServer
import org.gradle.api.Project
import java.net.URI

/**
 * @param name The name of the job in Jenkins
 * @param branch The branch corresponding to this simulation job
 * @param url The Jenkins URL of the Jenkins installation where this job is
 * @param credentials Credentials that allow for modifying this job
 */
data class SimulationJob(
    val name: String,
    val branch: String,
    val url: String,
    val credentials: JenkinsCredentials
) {
    companion object {
        fun usingPropertiesFromProject(project: Project): SimulationJob {
            if (!(project.hasProperty("branch") &&
                        project.hasProperty("url") &&
                        project.hasProperty("user") &&
                        project.hasProperty("password"))
            ) {
                throw RuntimeException(
                    "Missing project properties. Example usage:\n" +
                            "./gradlew :${project.projectDir.name}:createSimulationJob " +
                            "-Pbranch=simulation/1 " +
                            "-Purl=http://13.229.56.106:8080 " +
                            "-Puser=jenkins " +
                            "-Ppassword=secret\n" +
                            "Given properties were: ${project.properties}"
                )
            }

            val branch = project.property("branch") as String
            if (!branch.startsWith("simulation/")) {
                throw RuntimeException(
                    "Use a branch name that starts with 'simulation/'.\n" +
                            "For example, 'simulation/test' or 'simulation/8'\n" +
                            "Given branch name was '$branch'"
                )
            }
            if (branch.contains('_')) {
                throw RuntimeException(
                    "Use a branch name that doesn't contain the underscore character ( _ ).\n" +
                            "Given branch name was '$branch'"
                )
            }

            val jobName = "${project.projectDir.name}_${branch.replace('/', '-')}"
            return SimulationJob(
                jobName,
                branch,
                project.property("url") as String,
                JenkinsCredentials(
                    project.property("user") as String,
                    project.property("password") as String
                )
            )
        }
    }

    fun getJenkinsServer(): JenkinsServer = JenkinsServer(URI(url), credentials.user, credentials.password)
}