package hu.gyadam.tweetfeedtestapp.data.mapper

import androidx.compose.ui.platform.textInputServiceFactory
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import timber.log.Timber

fun TweetModel.toLoadedTweet(timeStamp: Long) = LoadedTweetModel(
    tweetTag = matching_rules[0].tag,
    tweetText = data.text,
    timeStamp = timeStamp
)

