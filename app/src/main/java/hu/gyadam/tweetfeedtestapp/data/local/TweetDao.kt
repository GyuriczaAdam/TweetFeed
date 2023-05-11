package hu.gyadam.tweetfeedtestapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TweetDao {

    @Query("SELECT * FROM TweetEntity")
    fun getTweets() : Flow<List<TweetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTweet(tweets:List<TweetEntity>)

    @Query("DELETE  FROM TweetEntity")
    suspend fun deleteAllTweets()
}