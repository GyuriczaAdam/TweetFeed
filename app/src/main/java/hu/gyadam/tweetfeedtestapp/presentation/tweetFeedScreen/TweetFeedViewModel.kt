package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.gyadam.tweetfeedtestapp.common.REQUEST_HEADER
import hu.gyadam.tweetfeedtestapp.common.Resource
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import hu.gyadam.tweetfeedtestapp.domain.useCase.GetTweetsFromDatabase
import hu.gyadam.tweetfeedtestapp.domain.useCase.TweetStreamReceiver
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TweetFeedViewModel @Inject constructor(
    private val tweetStreamreceiver: TweetStreamReceiver,
    private val connectivityObserver: ConnectivityObserver,
    private val getTweetsFromDatabase: GetTweetsFromDatabase,
    private val repository: TweetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TweetFeedState())
    val state: StateFlow<TweetFeedState> = _state
    private var tweetFeedJob: Job? = null
    private var lifeSpawnJob: Job? = null

    init {
        viewModelScope.launch {
            getTweetsFromDatabase().collectLatest { loadedTweets->
                _state.update {
                    it.copy(tweets = loadedTweets)
                }
            }
        }

    }

    fun onEvent(event: TweetFeedEvent) {
        when (event) {
            is TweetFeedEvent.ObserveConnectivity -> observeConnectivity(event.context)
            is TweetFeedEvent.GetTweetFeed -> getTweetFeed()
            is TweetFeedEvent.OnQueryChange -> _state.update { it.copy(query = event.query) }
            is TweetFeedEvent.OnSearchFocusChange ->
                _state.update { it.copy(isHintVisible = !event.isFocused && state.value.query.isBlank()) }
        }
    }

    private fun observeConnectivity(context: Context) {
        viewModelScope.launch {
            connectivityObserver.observer(context = context)
                .collectLatest { connectionStatus ->
                    _state.update {
                        it.copy(status = connectionStatus)
                    }
                    if (connectionStatus != ConnectivityObserver.Status.Available) {
                        stopTweetFeed()
                        repository.insertTweet(_state.value.tweets)
                    } else {
                        repository.deleteAllTweets()
                        if (_state.value.tweets.isNotEmpty()) {
                            _state.update {
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
            tweetStreamreceiver(REQUEST_HEADER)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            val updatedTweets = _state.value.tweets + result.data!!
                            _state.update {
                                it.copy(
                                    tweets = updatedTweets,
                                    isLoading = false,
                                    error = "",
                                )
                            }
                        }
                        is Resource.Loading -> {
                            if (_state.value.tweets.isEmpty()) {
                                _state.update {
                                    it.copy(isLoading = true)
                                }
                            }
                        }
                        is Resource.Error -> {
                            if (_state.value.tweets.isNotEmpty()) {
                                _state.update {
                                    it.copy(isLoading = false)
                                }
                            } else {
                                _state.update {
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
        lifeSpawnJob = viewModelScope.launch {
            while (true) {
                delay(1.seconds)
                if (_state.value.tweets.isNotEmpty()) {
                    if (isLongPropertyExpired(_state.value.tweets[0])) {
                        val updatedTweetsList = _state.value.tweets.drop(1)
                        _state.update {
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
