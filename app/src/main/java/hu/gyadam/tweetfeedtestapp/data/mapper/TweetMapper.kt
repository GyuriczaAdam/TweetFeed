package hu.gyadam.tweetfeedtestapp.data.mapper

import hu.gyadam.tweetfeedtestapp.common.TWEET_LIFE_SPAWN
import hu.gyadam.tweetfeedtestapp.data.local.TweetEntity
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel

fun TweetModel.toLoadedTweet(timeStamp: Long) = LoadedTweetModel(
    tweetTag = matching_rules[0].tag,
    tweetText = data.text,
    timeStamp = timeStamp
)

fun List<LoadedTweetModel>.toTweetEntityList(): List<TweetEntity> {
    return map {
        TweetEntity(
            matchingRule = it.tweetTag,
            tweetData = it.tweetText
        )
    }
}

fun List<TweetEntity>.toLoadedTweetModelList(): List<LoadedTweetModel> {
    return map {
        LoadedTweetModel(
            tweetTag = it.matchingRule,
            tweetText = it.tweetData,
            timeStamp = System.currentTimeMillis() + TWEET_LIFE_SPAWN
        )
    }
}


