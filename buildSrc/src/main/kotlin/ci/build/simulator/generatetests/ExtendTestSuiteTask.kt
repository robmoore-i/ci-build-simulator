package ci.build.simulator.generatetests

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Paths
import kotlin.random.Random

abstract class ExtendTestSuiteTask : DefaultTask() {

    @Input
    var testSourcesPath = "src/test/groovy"

    @get:Input
    abstract var stableTestsPackage: String

    @TaskAction
    fun extendTestSuite() {
        val dir = Paths.get("${project.projectDir}/$testSourcesPath/${stableTestsPackage.replace('.', '/')}").toFile()
        if (!dir.exists()) {
            throw RuntimeException("Can't find test sources dir. Tried '${dir.absolutePath}'.")
        }
        logger.quiet("Generating a stable test in ${dir.absolutePath}")
        val currentTestSourceFiles = dir.listFiles()?.filterNotNull() ?: emptyList()
        val nextTestNumber = nextTestNumber(currentTestSourceFiles)
        val newTest = File(dir, "StableTest$nextTestNumber.groovy")
        println("Generating file ${newTest.name}")
        newTest.writeText(
            """package ci.build.simulator.${project.projectDir.name}.stable

import org.junit.jupiter.api.Test

class StableTest${nextTestNumber} {
    @Test void t() {
        Thread.sleep(${randomTestDuration()})
        assert 1 == 1
    }
}
""".trimIndent()
        )
    }

    private fun nextTestNumber(currentTestSourceFiles: List<File>): Int {
        if (currentTestSourceFiles.isEmpty()) {
            return 1
        }
        val currentMaxTestNumber = currentTestSourceFiles.maxOf {
            it.nameWithoutExtension.drop("StableTest".length).toInt()
        }
        return currentMaxTestNumber + 1
    }

    private fun randomTestDuration(): Int {
        return Random.nextInt(1, 1000)
    }
}