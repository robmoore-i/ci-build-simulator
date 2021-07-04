package ci.build.simulator.sleeper.stable

import org.junit.jupiter.api.Test

class StableTest3 {
    @Test void t() {
        Thread.sleep(632)
        assert 1 == 1
    }
}