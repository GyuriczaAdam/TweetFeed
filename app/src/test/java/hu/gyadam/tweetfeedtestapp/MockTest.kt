package hu.gyadam.tweetfeedtestapp

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Before


interface MockTest {
    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    fun after() {
        clearAllMocks()
    }
}