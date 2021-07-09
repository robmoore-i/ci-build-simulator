package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest10 {
    @Test void t() {
        Thread.sleep(971)
        assert 1 == 1
    }
}