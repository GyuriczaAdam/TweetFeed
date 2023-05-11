package hu.gyadam.tweetfeedtestapp.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.gyadam.tweetfeedtestapp.BuildConfig
import hu.gyadam.tweetfeedtestapp.data.observer.ConnectivityObserverImpl
import hu.gyadam.tweetfeedtestapp.data.remote.TwitterApi
import hu.gyadam.tweetfeedtestapp.data.repository.TweetRepostioryImpl
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import hu.gyadam.tweetfeedtestapp.domain.useCase.RecieveTweetStream
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val gson = GsonBuilder()
        .setLenient()
        .create()
    val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    @Provides
    @Singleton
    fun provideTwitterApi(): TwitterApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
        .create(TwitterApi::class.java)

    @Provides
    @Singleton
    fun provideTweetRepository(api: TwitterApi): TweetRepository = TweetRepostioryImpl(api)


    @Provides
    @Singleton
    fun provideRecieveTweetStream(repository: TweetRepository): RecieveTweetStream =
        RecieveTweetStream(repository)

    @Provides
    @Singleton
    fun provideConnectivityObserver(): ConnectivityObserver =
        ConnectivityObserverImpl()
}