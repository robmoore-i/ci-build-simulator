package ci.build.simulator.jenkins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class CreateSimulationJobTask : DefaultTask() {
    @TaskAction
    fun create() {
        val job = SimulationJob.usingPropertiesFromProject(project)
        logger.quiet("Creating Jenkins job simulation for branch '${job.branch}'")
        val jenkins = job.getJenkinsServer()
        logger.quiet("Current jobs: ${jenkins.jobs}")
        if (jenkins.jobs.containsKey(job.name)) {
            throw RuntimeException("Job ${job.name} for branch '${job.branch}' already exists. Aborting.")
        }
        val jobXml = JenkinsJobTemplateSource.text(job.branch)
        logger.quiet("Create Jenkins job named '${job.name}'")
        logger.info("New job XML:\n-----\n$jobXml\n-----")
        jenkins.createJob(job.name, jobXml, true)
    }
}