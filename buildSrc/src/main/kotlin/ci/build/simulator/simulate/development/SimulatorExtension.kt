package ci.build.simulator.simulate.development

import org.gradle.api.provider.Property

abstract class SimulatorExtension {
    abstract val instance: Property<DevelopmentSimulator>
}