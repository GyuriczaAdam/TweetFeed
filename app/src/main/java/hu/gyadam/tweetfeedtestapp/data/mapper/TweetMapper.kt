package hu.gyadam.tweetfeedtestapp.data.mapper

import androidx.compose.ui.platform.textInputServiceFactory
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel

fun TweetModel.toLoadedTweet() = LoadedTweetModel(
    tweetTag = matching_rules[0].tag,
    tweetText = data.text
)