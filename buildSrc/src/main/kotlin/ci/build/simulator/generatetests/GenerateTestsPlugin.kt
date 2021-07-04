package ci.build.simulator.generatetests

import org.gradle.api.Plugin
import org.gradle.api.Project

class GenerateTestsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("extendTestSuite", ExtendTestSuiteTask::class.java) {
            group = "generateTests"
            description = "Extends the current test suite by generating more test classes."
            testSourcesPath = "src/test/groovy"
        }
    }
}