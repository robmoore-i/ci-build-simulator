package ci.build.simulator.jenkins

import com.offbytwo.jenkins.JenkinsServer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.net.URI

open class CreateJobTask : DefaultTask() {
    @TaskAction
    fun createJob() {
        CreateJobTaskInputValidator.validateTaskInputsFrom(project)

        val branch: String = project.property("branch") as String
        val url: String = project.property("url") as String
        val user: String = project.property("user") as String
        val password: String = project.property("password") as String

        logger.quiet("Creating Jenkins job simulation for branch '$branch'")
        val jenkins = JenkinsServer(URI(url), user, password)
        println("Current jobs: ${jenkins.jobs}")
        val jobName = "Build-$branch"
        if (jenkins.jobs.containsKey(jobName)) {
            throw RuntimeException("Job for branch '$branch' already exists. Aborting.")
        }
        val jobXml = JenkinsJobTemplateSource.text.replace("{{BRANCH}}", branch)
        println("Adding new job:\n-----\n$jobXml\n-----")
        jenkins.createJob(jobName, jobXml, true)

    }

}