package ci.build.simulator.simulate.development

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Paths
import kotlin.random.Random

abstract class SimulateDevelopmentTask : DefaultTask() {

    @Input
    var testSourcesPath = "src/test/groovy"

    @get:Input
    abstract var basePackage: String

    @TaskAction
    fun develop() {
        val dir = Paths.get("${project.projectDir}/$testSourcesPath/${basePackage.replace('.', '/')}").toFile()
        if (!dir.exists()) {
            throw RuntimeException("Can't find the resolved test sources dir. Tried '${dir.absolutePath}'.")
        }
        logger.quiet("Generating a test in ${dir.absolutePath}")
        val currentTestSourceFiles = dir.listFiles()?.filterNotNull() ?: emptyList()
        val nextTestNumber = nextTestNumber(currentTestSourceFiles)
        val testClassName = "${generatedClassBaseName}${nextTestNumber}"
        val newTest = File(dir, "${testClassName}.groovy")
        println("Generating file ${newTest.name}")
        newTest.writeText(
            """package $basePackage

import org.junit.jupiter.api.Test

class $testClassName {
    @Test void t() {
        Thread.sleep(${randomTestDuration()})
        assert 1 == 1
    }
}
""".trimIndent()
        )
    }

    companion object {
        private const val generatedClassBaseName = "DeterministicTest"

        private fun nextTestNumber(currentTestSourceFiles: List<File>): Int {
            val generatesTestFiles = currentTestSourceFiles.filter {
                it.nameWithoutExtension.startsWith(generatedClassBaseName)
            }
            if (generatesTestFiles.isEmpty()) {
                return 1
            }
            val currentMaxTestNumber = generatesTestFiles.maxOf {
                it.nameWithoutExtension.drop(generatedClassBaseName.length).toInt()
            }
            return currentMaxTestNumber + 1
        }

        private fun randomTestDuration(): Int {
            return Random.nextInt(1, 1000)
        }
    }
}