package ci.build.simulator.simulate.development

import org.gradle.api.logging.Logger
import java.io.File
import kotlin.random.Random

class SleeperDevelopmentSimulator(private val logger: Logger) : DevelopmentSimulator {
    override fun develop(basePackage: String, mainSourcesDir: File, testSourcesDir: File) {
        logger.quiet("Generating a test in ${testSourcesDir.absolutePath}")
        val currentTestSourceFiles = testSourcesDir.listFiles()?.filterNotNull() ?: emptyList()
        val nextTestNumber = nextTestNumber(currentTestSourceFiles)
        val testClassName = "${generatedClassBaseName}${nextTestNumber}"
        val newTest = File(testSourcesDir, "${testClassName}.groovy")
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