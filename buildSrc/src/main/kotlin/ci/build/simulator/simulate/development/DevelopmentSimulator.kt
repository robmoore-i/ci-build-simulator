package ci.build.simulator.simulate.development

import java.io.File

interface DevelopmentSimulator {
    fun develop(basePackage: String, mainSourcesDir: File, testSourcesDir: File)
}