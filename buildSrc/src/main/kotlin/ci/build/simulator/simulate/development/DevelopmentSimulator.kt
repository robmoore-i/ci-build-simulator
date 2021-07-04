package ci.build.simulator.simulate.development

import org.gradle.api.logging.Logger
import java.io.File

fun interface DevelopmentSimulator {
    fun develop(basePackage: String, mainSourcesDir: File, testSourcesDir: File)

    class NoopSimulator(private val logger: Logger) : DevelopmentSimulator {
        override fun develop(basePackage: String, mainSourcesDir: File, testSourcesDir: File) {
            logger.quiet(
                "No DevelopmentSimulator configured. " +
                        "Using ${NoopSimulator::class.java.simpleName} to generate nothing at all."
            )
        }
    }
}