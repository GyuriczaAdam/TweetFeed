package hu.gyadam.tweetfeedtestapp.testData

import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel

object TweetListTestData {
    val tweets = listOf(
        LoadedTweetModel(
            "Tag",
            "Text",
            2000
        ),
        LoadedTweetModel(
            "Tag",
            "Text",
            2000
        ),
        LoadedTweetModel(
            "Tag",
            "Text",
            2000
        ),
    )
}