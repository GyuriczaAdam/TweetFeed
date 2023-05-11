package hu.gyadam.tweetfeedtestapp

import android.content.Context
import app.cash.turbine.test
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import hu.gyadam.tweetfeedtestapp.domain.useCase.GetTweetsFromDatabase
import hu.gyadam.tweetfeedtestapp.domain.useCase.TweetStreamReceiver
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.TweetFeedEvent
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.TweetFeedViewModel
import hu.gyadam.tweetfeedtestapp.testData.TweetListTestData
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
    private val tweetStreamreceiver: TweetStreamReceiver = mockk(relaxed = true)

    @MockK
    private val mockContext: Context = mockk(relaxed = true)

    @MockK
    private val connectivityObserver: ConnectivityObserver = mockk(relaxed = true) {
        every { observer(mockContext) } returns flowOf(ConnectivityObserver.Status.Available)
    }

    @MockK
    private val getTweetsFromDatabase: GetTweetsFromDatabase = mockk(relaxed = true)

    @MockK
    private val repository: TweetRepository = mockk(relaxed = true)


    override fun createViewModel(): TweetFeedViewModel =
        TweetFeedViewModel(
            tweetStreamreceiver,
            connectivityObserver,
            getTweetsFromDatabase,
            repository
        )

    @Test
    fun `WHEN viewModel initalized THEN tweets are loaded from database if there is no connectivity`() =
        runTest {
            every { getTweetsFromDatabase() } returns flowOf(TweetListTestData.tweets)
            withViewModelUnderTest {
                advanceUntilIdle()
                state.test {
                    val result = awaitItem()
                    assertEquals(result.tweets,TweetListTestData.tweets)
                }
            }
        }

    @Test
    fun `GIVEN an initalized viewModel WHEN queryChanged method THEN state is updated`() =
        runTest {
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
