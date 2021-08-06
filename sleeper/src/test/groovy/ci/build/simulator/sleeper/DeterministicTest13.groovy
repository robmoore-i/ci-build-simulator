package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest13 {
    @Test void t() {
        Thread.sleep(961)
        assert 1 == 1
    }
}