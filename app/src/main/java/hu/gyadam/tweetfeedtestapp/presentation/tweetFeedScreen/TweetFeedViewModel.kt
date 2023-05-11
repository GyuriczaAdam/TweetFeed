package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.gyadam.tweetfeedtestapp.common.REQUEST_HEADER
import hu.gyadam.tweetfeedtestapp.common.Resource
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.domain.useCase.RecieveTweetStream
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


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
            is TweetFeedEvent.GetTweetFeed -> getTweetFeed()
            is TweetFeedEvent.OnQueryChange -> state.update { it.copy(query = event.query) }
            is TweetFeedEvent.OnSearchFocusChange -> state.update { it.copy(isHintVisible = !event.isFocused && state.value.query.isBlank()) }
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
                    }else {
                        if(state.value.tweets.isNotEmpty()) {
                            state.update {
                                it.copy(
                                    tweets = emptyList(),
                                    isLoading = true
                                )
                            }
                            tryToGetTweets()
                        }
                    }
                }
        }
    }


    private fun getTweetFeed() {
        observerTweetLifeSpawn()
        tweetFeedJob?.cancel()
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
            //If I do not put a delay then it will return an error because it is too fast for the api
            delay(5.seconds)
            getTweetFeed()
        }
    }

    private fun observerTweetLifeSpawn() {
        lifeSpawnJob?.cancel()
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
    }

    private fun isLongPropertyExpired(tweet: LoadedTweetModel): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime >= tweet.timeStamp
    }

}

