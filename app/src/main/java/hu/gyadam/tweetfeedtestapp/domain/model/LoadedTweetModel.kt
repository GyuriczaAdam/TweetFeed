package hu.gyadam.tweetfeedtestapp.domain.model

data class LoadedTweetModel(
    val tweetTag : String,
    val tweetText : String,
    val timeStamp : Long
)
