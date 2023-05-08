package hu.gyadam.tweetfeedtestapp.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import hu.gyadam.tweetfeedtestapp.BuildConfig
import hu.gyadam.tweetfeedtestapp.common.Resource
import hu.gyadam.tweetfeedtestapp.data.mapper.toLoadedTweet
import hu.gyadam.tweetfeedtestapp.data.remote.TwitterApi
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URISyntaxException
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


class TweetRepostioryImpl @Inject constructor(
    private val api: TwitterApi,
) : TweetRepository {
    override suspend fun getTweets(token: String): Flow<Resource<LoadedTweetModel>> = flow {
        emit(Resource.Loading())
        val gson = Gson()
        val response = api.getTweets(token).byteStream()
        val reader = BufferedReader(InputStreamReader(response))
        try {
            while (true) {
                delay(1.seconds)
                val line = reader.readLine() ?: break
                emit(Resource.Success(gson.fromJson(line, TweetModel::class.java).toLoadedTweet()) )
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage))
            Timber.e("Error happend : ${e.message}")
        } finally {
            reader.close()
        }
    }.buffer()
}