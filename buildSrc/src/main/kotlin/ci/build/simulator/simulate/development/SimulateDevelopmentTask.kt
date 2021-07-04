package ci.build.simulator.simulate.development

import ci.build.simulator.simulate.development.simulators.DevelopmentSimulator
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths

abstract class SimulateDevelopmentTask : DefaultTask() {

    @Input
    var mainSourcesPath = "src/main/groovy"

    @Input
    var testSourcesPath = "src/test/groovy"

    @get:Input
    abstract var basePackage: String

    @get:Input
    abstract var simulator: DevelopmentSimulator

    @TaskAction
    fun develop() {
        val packageFolders = basePackage.replace('.', '/')

        val mainSourcesDir = Paths.get("${project.projectDir}/$mainSourcesPath/$packageFolders").toFile()
        if (!mainSourcesDir.exists()) {
            throw RuntimeException("Can't find the resolved main sources dir. Tried '${mainSourcesDir.absolutePath}'.")
        }

        val testSourcesDir = Paths.get("${project.projectDir}/$testSourcesPath/$packageFolders").toFile()
        if (!testSourcesDir.exists()) {
            throw RuntimeException("Can't find the resolved test sources dir. Tried '${testSourcesDir.absolutePath}'.")
        }

        simulator.develop(basePackage, mainSourcesDir, testSourcesDir)
    }
}