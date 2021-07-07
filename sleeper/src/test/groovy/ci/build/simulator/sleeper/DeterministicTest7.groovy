package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest7 {
    @Test void t() {
        Thread.sleep(649)
        assert 1 == 1
    }
}