package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest9 {
    @Test void t() {
        Thread.sleep(457)
        assert 1 == 1
    }
}