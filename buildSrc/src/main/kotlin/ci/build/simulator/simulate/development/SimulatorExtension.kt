package ci.build.simulator.simulate.development

import ci.build.simulator.simulate.development.simulators.DevelopmentSimulator
import org.gradle.api.provider.Property

abstract class SimulatorExtension {
    abstract val instance: Property<DevelopmentSimulator>
}