package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest4 {
    @Test void t() {
        Thread.sleep(575)
        assert 1 == 1
    }
}