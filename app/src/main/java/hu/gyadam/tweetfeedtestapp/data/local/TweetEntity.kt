package hu.gyadam.tweetfeedtestapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TweetEntity(
    @PrimaryKey val id : Int?  = null,
    val tweetData : String,
    val matchingRule: String
)
