package ci.build.simulator.jenkins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class DeleteSimulationJobTask : DefaultTask() {
    @TaskAction
    fun delete() {
        val job = SimulationJob.usingPropertiesFromProject(project)
        val jenkins = job.getJenkinsServer()
        if (jenkins.jobs.containsKey(job.name)) {
            jenkins.deleteJob(job.name, true)
            logger.quiet("Deleted jenkins job ${job.name}")
        }
        if (jenkins.jobs.containsKey(job.name)) {
            throw RuntimeException("Deleting job ${job.name} from Jenkins server failed. Aborting.")
        }

        val git = Git(project)
        if (git.isBranchAlreadyExistingLocally(job.branch)) {
            git.deleteBranchLocally(job.branch)
        }
        if (git.isBranchAlreadyExistingRemotely(job.branch)) {
            git.deleteBranchRemotely(job.branch)
        }
    }
}