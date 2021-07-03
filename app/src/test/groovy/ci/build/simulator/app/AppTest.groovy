package ci.build.simulator.app

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

class AppTest {
    @Test void 'says hello'() {
        assertEquals('Hello world!', App.run())
    }
}
