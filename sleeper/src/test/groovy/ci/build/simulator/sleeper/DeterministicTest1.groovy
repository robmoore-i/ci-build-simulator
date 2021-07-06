package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest1 {
    @Test void t() {
        Thread.sleep(169)
        assert 1 == 1
    }
}