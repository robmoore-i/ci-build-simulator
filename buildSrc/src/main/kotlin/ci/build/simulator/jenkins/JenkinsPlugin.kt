package ci.build.simulator.jenkins

import org.gradle.api.Plugin
import org.gradle.api.Project

class JenkinsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("createSimulationJob", CreateSimulationJobTask::class.java) {
            group = "jenkins"
            description = "Creates a Jenkins simulation job for the given branch of this repo"
        }
        target.tasks.register("deleteSimulationJob", DeleteSimulationJobTask::class.java) {
            group = "jenkins"
            description = "Deletes a Jenkins simulation job for the given branch of this repo"
        }
    }
}