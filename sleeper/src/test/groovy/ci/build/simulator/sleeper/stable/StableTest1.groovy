package ci.build.simulator.sleeper.stable

import org.junit.jupiter.api.Test

class StableTest1 {
    @Test void t() {
        Thread.sleep(100)
        assert 1 == 1
    }
}
