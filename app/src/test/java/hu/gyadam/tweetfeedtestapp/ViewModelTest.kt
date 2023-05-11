package hu.gyadam.tweetfeedtestapp

import hu.gyadam.tweetfeedtestapp.util.MainCoroutineRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Rule

abstract class ViewModelTest<T : Any>(testDispatcher: TestDispatcher = StandardTestDispatcher()) :
    MockTest {

    @get:Rule
    val coroutineRule: MainCoroutineRule = MainCoroutineRule(testDispatcher)

    abstract fun createViewModel(): T

    protected inline fun withViewModelUnderTest(action: T.() -> Unit) =
        with(createViewModel()) { action() }

}
