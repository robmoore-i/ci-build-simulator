package ci.build.simulator.simulate.development

import org.gradle.api.Plugin
import org.gradle.api.Project

class SimulateDevelopmentPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("simulateDevelopment", SimulateDevelopmentTask::class.java) {
            group = "simulate development"
            description = "Modifies the source code to simulate development."
            testSourcesPath = "src/test/groovy"
            basePackage = "ci.build.simulator.${project.projectDir.name}"
        }
    }
}