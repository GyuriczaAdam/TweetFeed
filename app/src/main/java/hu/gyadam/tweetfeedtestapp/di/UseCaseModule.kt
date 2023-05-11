package hu.gyadam.tweetfeedtestapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import hu.gyadam.tweetfeedtestapp.data.observer.ConnectivityObserverImpl
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.domain.repository.TweetRepository
import hu.gyadam.tweetfeedtestapp.domain.useCase.GetTweetsFromDatabase
import hu.gyadam.tweetfeedtestapp.domain.useCase.TweetStreamReceiver

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideRecieveTweetStream(repository: TweetRepository): TweetStreamReceiver =
        TweetStreamReceiver(repository)

    @Provides
    @ViewModelScoped
    fun provideConnectivityObserver(): ConnectivityObserver =
        ConnectivityObserverImpl()

    @Provides
    @ViewModelScoped
    fun provideGetTweetsFromDatabase(repository: TweetRepository) : GetTweetsFromDatabase = GetTweetsFromDatabase(repository)
}