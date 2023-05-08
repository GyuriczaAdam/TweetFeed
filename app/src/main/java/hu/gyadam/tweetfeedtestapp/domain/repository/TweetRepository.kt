package hu.gyadam.tweetfeedtestapp.domain.repository

import androidx.lifecycle.MutableLiveData
import hu.gyadam.tweetfeedtestapp.common.Resource
import hu.gyadam.tweetfeedtestapp.data.remote.dto.TweetModel
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface TweetRepository {
   suspend fun getTweets(token : String) : Flow<Resource<LoadedTweetModel>>
}