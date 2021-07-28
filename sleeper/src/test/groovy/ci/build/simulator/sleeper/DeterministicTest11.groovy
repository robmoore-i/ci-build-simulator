package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest11 {
    @Test void t() {
        Thread.sleep(927)
        assert 1 == 1
    }
}