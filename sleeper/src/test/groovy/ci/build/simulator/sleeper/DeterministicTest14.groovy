package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest14 {
    @Test void t() {
        Thread.sleep(888)
        assert 1 == 1
    }
}