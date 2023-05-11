package hu.gyadam.tweetfeedtestapp.domain.repository

import hu.gyadam.tweetfeedtestapp.data.local.TweetEntity
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import java.io.InputStream
import kotlinx.coroutines.flow.Flow

interface TweetRepository {
    suspend fun getTweets(token: String): InputStream

    suspend fun insertTweet(list: List<LoadedTweetModel>)

    fun getAllTweets(): Flow<List<TweetEntity>>

    suspend fun deleteAllTweets()
}