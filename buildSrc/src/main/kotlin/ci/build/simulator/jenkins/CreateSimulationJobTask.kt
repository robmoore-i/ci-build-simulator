package ci.build.simulator.jenkins

import com.offbytwo.jenkins.JenkinsServer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class CreateSimulationJobTask : DefaultTask() {
    @TaskAction
    fun create() {
        val job = SimulationJob.usingPropertiesFromProject(project)
        val jenkins = job.getJenkinsServer()
        logger.info("Current jobs: ${jenkins.jobs}")
        if (jenkins.jobs.containsKey(job.name)) {
            throw RuntimeException("Job ${job.name} for branch '${job.branch}' already exists. Aborting.")
        }
        createSimulationBranch(Git(project), job)
        createSimulationJenkinsJob(job, jenkins)
    }

    private fun createSimulationBranch(git: Git, job: SimulationJob) {
        git.fetchAll()
        if (git.isBranchAlreadyExistingRemotely(job.branch)) {
            logger.info("Branch ${job.branch} already exists remotely, not recreating it.")
            return
        }

        git.verifyLocalWorkingTreeIsClean()
        val originalBranch = git.currentBranch()
        if (git.isBranchAlreadyExistingLocally(job.branch)) {
            git.checkoutExistingBranch(job.branch)
        } else {
            git.checkoutNewBranch(job.branch)
        }
        git.up(job.branch)
        git.checkoutExistingBranch(originalBranch)
    }

    private fun createSimulationJenkinsJob(
        job: SimulationJob,
        jenkins: JenkinsServer
    ) {
        val jobXml = JenkinsJobTemplateSource.text(job.branch)
        logger.quiet("Create Jenkins job named '${job.name}'")
        logger.info("New job XML:\n-----\n$jobXml\n-----")
        jenkins.createJob(job.name, jobXml, true)
    }
}
