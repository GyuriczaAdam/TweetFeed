package hu.gyadam.tweetfeedtestapp.data.remote

import androidx.core.app.NotificationCompat.StreamType
import hu.gyadam.tweetfeedtestapp.BuildConfig
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import java.io.InputStream
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming

interface TwitterApi {

    @GET("tweets/search/stream")
    @Streaming
    suspend fun getTweets(
        @Header("Authorization") token:String
    ) : ResponseBody
}