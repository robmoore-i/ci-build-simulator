package ci.build.simulator.jenkins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class CreateJobTask : DefaultTask() {
    @TaskAction
    fun createJob() {
        val inputs = CreateJobTaskInputs.usingPropertiesFromProject(project)
        logger.quiet("Creating Jenkins job simulation for branch '${inputs.branch}'")
        val jenkins = inputs.getJenkinsServer()
        logger.quiet("Current jobs: ${jenkins.jobs}")
        val jobName = "Build-${inputs.branch}"
        if (jenkins.jobs.containsKey(jobName)) {
            throw RuntimeException("Job for branch '${inputs.branch}' already exists. Aborting.")
        }
        val jobXml = JenkinsJobTemplateSource.text.replace("{{BRANCH}}", inputs.branch)
        logger.quiet("Create Jenkins job named '$jobName'")
        logger.info("New job XML:\n-----\n$jobXml\n-----")
        jenkins.createJob(jobName, jobXml, true)
    }
}