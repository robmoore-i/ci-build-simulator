package ci.build.simulator.jenkins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class DeleteSimulationJobTask : DefaultTask() {
    @TaskAction
    fun delete() {
        val job = SimulationJob.usingPropertiesFromProject(project)
        logger.quiet("Deleting Jenkins job simulation for branch '${job.branch}'")
        val jenkins = job.getJenkinsServer()
        if (jenkins.jobs.containsKey(job.name)) {
            jenkins.deleteJob(job.name, true)
        }
        if (jenkins.jobs.containsKey(job.name)) {
            throw RuntimeException("Deleting job ${job.name} from Jenkins server failed. Aborting.")
        }

        project.exec {
            commandLine("git", "branch", "-D", job.branch)
        }.assertNormalExitValue()
        project.exec {
            commandLine("git", "push", "--delete", "origin", job.branch)
        }.assertNormalExitValue()
    }
}