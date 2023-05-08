package hu.gyadam.tweetfeedtestapp.presentation.util

sealed class Screens (val route :String) {
    object TweetFeedScreen : Screens("tweet_feed")
}