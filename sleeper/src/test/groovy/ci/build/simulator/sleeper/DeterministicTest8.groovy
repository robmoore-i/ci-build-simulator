package ci.build.simulator.sleeper

import org.junit.jupiter.api.Test

class DeterministicTest8 {
    @Test void t() {
        Thread.sleep(791)
        assert 1 == 1
    }
}