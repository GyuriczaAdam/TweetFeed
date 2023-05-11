package hu.gyadam.tweetfeedtestapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.gyadam.tweetfeedtestapp.BuildConfig
import hu.gyadam.tweetfeedtestapp.common.CONNECTION_TIMEOUT
import hu.gyadam.tweetfeedtestapp.data.local.TweetDatabase
import hu.gyadam.tweetfeedtestapp.data.remote.TwitterApi
import hu.gyadam.tweetfeedtestapp.data.repository.TweetRepostioryImpl
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val client = OkHttpClient.Builder()
        .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideTwitterApi(): TwitterApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .build()
        .create(TwitterApi::class.java)

    @Provides
    @Singleton
    fun provideTweetRepository(api: TwitterApi, db: TweetDatabase): TweetRepository =
        TweetRepostioryImpl(api, db.tweetDao)


    @Provides
    @Singleton
    fun provideTweetDatabase(app: Application): TweetDatabase {
        return Room.databaseBuilder(
            app,
            TweetDatabase::class.java,
            TweetDatabase.DATBASE_NAME
        ).build()
    }
}