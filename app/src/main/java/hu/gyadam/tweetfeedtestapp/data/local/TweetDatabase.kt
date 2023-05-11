package hu.gyadam.tweetfeedtestapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TweetEntity::class],
    version = 1
)
abstract class TweetDatabase : RoomDatabase() {
    abstract val tweetDao : TweetDao
    companion object {
        const val DATBASE_NAME ="tweet_database"
    }
}