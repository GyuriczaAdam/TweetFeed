package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.gyadam.tweetfeedtestapp.BuildConfig
import hu.gyadam.tweetfeedtestapp.common.Resource
import hu.gyadam.tweetfeedtestapp.data.remote.TwitterApi
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@HiltViewModel
class TweetFeedViewModel @Inject constructor(
    private val tweetRepository: TweetRepository,
    private val api: TwitterApi,
) : ViewModel() {

    val state = MutableStateFlow(TweetFeedState())
    val tweetList = MutableStateFlow(emptyList<LoadedTweetModel>())

    init {
        viewModelScope.launch {
            tweetRepository.getTweets("Bearer ${BuildConfig.BEARER_TOKEN}")
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            state.update {
                                it.copy(
                                    tweets = state.value.tweets + result.data!!,
                                    isLoading = false
                                )
                            }
                        }

                        is Resource.Loading -> {
                            state.update {
                                it.copy(isLoading = true)
                            }
                        }

                        is Resource.Error -> {
                            state.update {
                                it.copy(error = result.message!!)
                            }
                        }
                    }
                }

        }
    }
}

