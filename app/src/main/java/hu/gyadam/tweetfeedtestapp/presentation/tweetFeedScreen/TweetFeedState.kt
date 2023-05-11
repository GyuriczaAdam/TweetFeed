package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver

data class TweetFeedState(
    val isLoading: Boolean = false,
    val query: String = "",
    val tweets: List<LoadedTweetModel> = emptyList(),
    val error: String = "",
    val isHintVisible: Boolean = false,
    val status: ConnectivityObserver.Status = ConnectivityObserver.Status.Lost,
)
