package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel

data class TweetFeedState(
    val isLoading :Boolean = false,
    val tweets : List<LoadedTweetModel> = emptyList(),
    val error : String = ""
)
