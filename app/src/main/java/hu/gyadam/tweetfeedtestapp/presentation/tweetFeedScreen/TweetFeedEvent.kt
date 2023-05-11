package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import android.content.Context

sealed class TweetFeedEvent {

    data class ObserveConnectivity(val context: Context) : TweetFeedEvent()
    object GetTweetFeed :TweetFeedEvent()
}
