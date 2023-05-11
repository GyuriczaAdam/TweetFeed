package hu.gyadam.tweetfeedtestapp

import android.content.Context
import app.cash.turbine.test
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.domain.useCase.RecieveTweetStream
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.TweetFeedEvent
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.TweetFeedViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TweetFeedViewModelTest : ViewModelTest<TweetFeedViewModel>() {

    @MockK
    private val recieveTweetStream: RecieveTweetStream = mockk(relaxed = true)

    @MockK
    private val mockContext: Context = mockk(relaxed = true)

    @MockK
    private val connectivityObserver: ConnectivityObserver = mockk(relaxed = true) {
        every { observer(mockContext) } returns flowOf(ConnectivityObserver.Status.Available)
    }

    override fun createViewModel(): TweetFeedViewModel =
        TweetFeedViewModel(recieveTweetStream, connectivityObserver)


    @Test
    fun `GIVEN an initalized viewModel WHEN getTweets method returns an error THEN state is updated`() =
        runTest {
            every { connectivityObserver.observer(mockContext) } returns flowOf(ConnectivityObserver.Status.Available)
            withViewModelUnderTest {
                advanceUntilIdle()
                onEvent(TweetFeedEvent.OnQueryChange("1"))

                state.test {
                    val result = awaitItem()
                    assertEquals(result.query, "1")
                }
            }
        }
}
