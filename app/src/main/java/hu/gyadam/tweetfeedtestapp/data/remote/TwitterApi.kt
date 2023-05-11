package hu.gyadam.tweetfeedtestapp.data.remote

import okhttp3.ResponseBody
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