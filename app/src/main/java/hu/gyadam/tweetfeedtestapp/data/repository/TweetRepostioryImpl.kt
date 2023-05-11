package hu.gyadam.tweetfeedtestapp.data.repository

import hu.gyadam.tweetfeedtestapp.data.local.TweetDao
import hu.gyadam.tweetfeedtestapp.data.mapper.toTweetEntityList
import hu.gyadam.tweetfeedtestapp.data.remote.TwitterApi
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import java.io.InputStream
import javax.inject.Inject


class TweetRepostioryImpl @Inject constructor(
    private val api: TwitterApi,
    private val dao: TweetDao,
) : TweetRepository {
    override suspend fun getTweets(token: String): InputStream = api.getTweets(token).byteStream()
    override suspend fun insertTweet(list: List<LoadedTweetModel>) =
        dao.insertTweet(list.toTweetEntityList())

    override fun getAllTweets() = dao.getTweets()
    override suspend fun deleteAllTweets() = dao.deleteAllTweets()


}