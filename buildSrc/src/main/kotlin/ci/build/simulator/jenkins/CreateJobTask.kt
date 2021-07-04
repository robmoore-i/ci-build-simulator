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
        if (jenkins.jobs.containsKey(inputs.jobName)) {
            throw RuntimeException("Job ${inputs.jobName} for branch '${inputs.branch}' already exists. Aborting.")
        }
        val jobXml = JenkinsJobTemplateSource.text(inputs.branch, project.projectDir.name)
        logger.quiet("Create Jenkins job named '${inputs.jobName}'")
        logger.info("New job XML:\n-----\n$jobXml\n-----")
        jenkins.createJob(inputs.jobName, jobXml, true)
    }
}