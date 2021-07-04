package ci.build.simulator.sleeper.stable

import org.junit.jupiter.api.Test

class StableTest2 {
    @Test void t() {
        Thread.sleep(668)
        assert 1 == 1
    }
}