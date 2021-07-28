package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest12 {
    @Test void t() {
        Thread.sleep(295)
        assert 1 == 1
    }
}