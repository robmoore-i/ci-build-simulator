package ci.build.simulator.jenkins

import com.offbytwo.jenkins.JenkinsServer
import org.gradle.api.Project
import java.io.File
import java.net.URI

/**
 * @param name The name of the job in Jenkins
 * @param branch The branch corresponding to this simulation job
 * @param url The Jenkins URL of the Jenkins installation where this job is
 * @param owner Credentials of a Jenkins user who is permitted to modify this job
 */
data class SimulationJob(
    val name: String,
    val branch: String,
    val url: String,
    val owner: JenkinsCredentials
) {
    companion object {
        private const val jenkinsProperties = "buildSrc/src/main/resources/jenkins.properties"

        fun usingPropertiesFromProject(project: Project): SimulationJob {
            var url = project.findProperty("url") as String?
            var user = project.findProperty("user") as String?
            var password = project.findProperty("password") as String?
            val jenkinsProperties = File("${project.rootProject.projectDir.absolutePath}/$jenkinsProperties")
            if (jenkinsProperties.exists()) {
                val lines = jenkinsProperties.readLines()
                url = lines.firstOrNull { it.startsWith("url=") }?.drop("url=".length)?.trim()
                user = lines.firstOrNull { it.startsWith("user=") }?.drop("user=".length)?.trim()
                password = lines.firstOrNull { it.startsWith("password=") }?.drop("password=".length)?.trim()
            }
            val branch = project.findProperty("branch") as String?
            if (branch == null || url == null || user == null || password == null) {
                throw RuntimeException(
                    "Missing project properties. Example usage:\n" +
                            "./gradlew :${project.projectDir.name}:createSimulationJob " +
                            "-Pbranch=simulation/1 " +
                            "-Purl=http://13.229.56.106:8080 " +
                            "-Puser=jenkins " +
                            "-Ppassword=secret\n" +
                            "Given properties were: ${project.properties}.\n" +
                            "You can also use 'jenkins.properties'. Check the README for usage."
                )
            }

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
            return SimulationJob(jobName, branch, url, JenkinsCredentials(user, password))
        }
    }

    fun getJenkinsServer(): JenkinsServer = JenkinsServer(URI(url), owner.user, owner.password)
}