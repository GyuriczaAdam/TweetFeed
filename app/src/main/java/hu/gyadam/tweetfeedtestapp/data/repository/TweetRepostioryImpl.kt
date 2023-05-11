package hu.gyadam.tweetfeedtestapp.data.repository

import com.google.gson.Gson
import hu.gyadam.tweetfeedtestapp.common.Resource
import hu.gyadam.tweetfeedtestapp.common.TWEET_LIFE_SPAWN
import hu.gyadam.tweetfeedtestapp.data.mapper.toLoadedTweet
import hu.gyadam.tweetfeedtestapp.data.remote.TwitterApi
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker


class TweetRepostioryImpl @Inject constructor(
    private val api: TwitterApi,
) : TweetRepository {
    /* override suspend fun getTweets(token: String): Flow<Resource<LoadedTweetModel>> = flow {
        emit(Resource.Loading())
        val gson = Gson()
        try {
            val response = api.getTweets(token).byteStream()
            val reader = BufferedReader(InputStreamReader(response))
            try {
                while (true) {
                    val ticker = ticker(delayMillis = 1000)
                    val line = reader.readLine() ?: throw Exception("Empty line")
                    emit(
                        Resource.Success(
                            gson.fromJson(line, TweetModel::class.java)
                                .toLoadedTweet(System.currentTimeMillis() + TWEET_LIFE_SPAWN)
                        )
                    )
                    ticker.receive()
                }
            } catch (e: IOException) {
                emit(Resource.Error(e.localizedMessage))
            } finally {
                reader.close()
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }.flowOn(Dispatchers.IO)*/
    override suspend fun getTweets(token: String) : InputStream = api.getTweets(token).byteStream()

}