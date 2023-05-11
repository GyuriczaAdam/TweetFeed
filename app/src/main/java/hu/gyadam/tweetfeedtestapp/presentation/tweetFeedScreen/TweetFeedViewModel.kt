package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.gyadam.tweetfeedtestapp.BuildConfig
import hu.gyadam.tweetfeedtestapp.common.REQUEST_HEADER
import hu.gyadam.tweetfeedtestapp.common.Resource
import hu.gyadam.tweetfeedtestapp.data.remote.TwitterApi
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import hu.gyadam.tweetfeedtestapp.domain.useCase.RecieveTweetStream
import hu.gyadam.tweetfeedtestapp.presentation.util.UiEvent
import hu.gyadam.tweetfeedtestapp.presentation.util.UiText
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber


@HiltViewModel
class TweetFeedViewModel @Inject constructor(
    private val recieveTweetStream: RecieveTweetStream,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    val state = MutableStateFlow(TweetFeedState())
    private var tweetFeedJob: Job? = null
    private var lifeSpawnJob: Job? = null


    fun onEvent(event: TweetFeedEvent) {
        when (event) {
            is TweetFeedEvent.ObserveConnectivity -> observeConnectivity(event.context)
            TweetFeedEvent.GetTweetFeed -> getTweetFeed()
        }
    }

    private fun observeConnectivity(context: Context) {
        viewModelScope.launch {
            connectivityObserver.observer(context = context)
                .collectLatest { connectionStatus ->
                    state.update {
                        it.copy(status = connectionStatus)
                    }
                    if (connectionStatus != ConnectivityObserver.Status.Available) {
                        stopTweetFeed()
                    } else {
                        getTweetFeed()
                        observerTweetLifeSpawn()
                    }
                }
        }
    }


    private fun getTweetFeed() {
        tweetFeedJob = viewModelScope.async {
            recieveTweetStream(REQUEST_HEADER)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            val updatedTweets = state.value.tweets + result.data!!
                            state.update {
                                it.copy(
                                    tweets = updatedTweets,
                                    isLoading = false,
                                    error = "",
                                )
                            }
                        }

                        is Resource.Loading -> {
                            if (!state.value.tweets.isNotEmpty()) {
                                state.update {
                                    it.copy(isLoading = true)
                                }
                            }
                        }

                        is Resource.Error -> {
                            tryToGetTweets()
                            if (state.value.tweets.isNotEmpty()) {
                                state.update {
                                    it.copy(isLoading = false)
                                }
                            } else {
                                state.update {
                                    it.copy(error = result.message!!, isLoading = false)
                                }
                            }
                        }
                    }
                }
        }
    }


    private fun tryToGetTweets() {
        viewModelScope.launch {
            delay(10.seconds)
            stopTweetFeed()
            getTweetFeed()
        }
    }

    private fun observerTweetLifeSpawn() {
        lifeSpawnJob = viewModelScope.async {
            while (true) {
                delay(1.seconds)
                if (state.value.tweets.isNotEmpty()) {
                    if (isLongPropertyExpired(state.value.tweets[0])) {
                        val updatedTweetsList = state.value.tweets.drop(1)
                        state.update {
                            it.copy(tweets = updatedTweetsList)
                        }
                    }
                }
            }
        }

    }

    private fun stopTweetFeed() {
        lifeSpawnJob?.cancel()
        tweetFeedJob?.cancel()
        lifeSpawnJob = null
        tweetFeedJob = null
    }

    private fun isLongPropertyExpired(tweet: LoadedTweetModel): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime >= tweet.timeStamp
    }

}

