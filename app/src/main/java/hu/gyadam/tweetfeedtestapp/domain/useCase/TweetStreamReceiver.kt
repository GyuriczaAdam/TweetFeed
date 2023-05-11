package hu.gyadam.tweetfeedtestapp.domain.useCase

import com.google.gson.Gson
import hu.gyadam.tweetfeedtestapp.common.Resource
import hu.gyadam.tweetfeedtestapp.common.TWEET_LIFE_SPAWN
import hu.gyadam.tweetfeedtestapp.data.mapper.toLoadedTweet
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class TweetStreamReceiver @Inject constructor(
    private val repository: TweetRepository,
) {
     @OptIn(ObsoleteCoroutinesApi::class)
     suspend operator fun invoke(token :String): Flow<Resource<LoadedTweetModel>> = flow  {
         emit(Resource.Loading())
         val gson = Gson()
         try {
             val response = repository.getTweets(token)
             val reader = BufferedReader(InputStreamReader(response))
             try {
                 while (reader.readLine() != null) {
                     val line = reader.readLine()
                     val ticker = ticker(delayMillis = 1000)
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
             emit(Resource.Error(e.localizedMessage!!))
         } catch (e: Exception) {
             emit(Resource.Error(e.localizedMessage!!))
         }
     }.flowOn(Dispatchers.IO)

}