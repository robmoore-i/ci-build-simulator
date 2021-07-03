package ci.build.simulator

import org.gradle.api.Plugin
import org.gradle.api.Project

class GenerateTestsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("generateStableTest", GenerateStableTestTask::class.java) {
            group = "generateTests"
            description = "Generates stable (non-flaky) tests."
            testSourcesPath = "src/test/groovy"
        }
    }
}