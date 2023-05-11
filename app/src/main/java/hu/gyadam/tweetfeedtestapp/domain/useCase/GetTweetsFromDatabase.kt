package hu.gyadam.tweetfeedtestapp.domain.useCase

import hu.gyadam.tweetfeedtestapp.data.mapper.toLoadedTweetModelList
import hu.gyadam.tweetfeedtestapp.domain.model.LoadedTweetModel
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTweetsFromDatabase(
    private val repository: TweetRepository,
) {
    operator fun invoke(): Flow<List<LoadedTweetModel>> = repository.getAllTweets().map {
        it.toLoadedTweetModelList()
    }
}