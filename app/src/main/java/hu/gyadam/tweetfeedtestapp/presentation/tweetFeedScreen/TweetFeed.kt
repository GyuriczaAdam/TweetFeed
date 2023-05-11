package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.components.ErrorComponent
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.components.HeaderComponent
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.components.SearchTextField
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.components.TweetCard
import hu.gyadam.tweetfeedtestapp.presentation.util.LocalSpacing

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TweetFeed(
    viewModel: TweetFeedViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.onEvent(TweetFeedEvent.ObserveConnectivity(context))
    }
    Column(
        modifier = Modifier
            .padding(spacing.spaceMedium)
            .fillMaxSize()
    ) {
        HeaderComponent(status = state.status.name)
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        SearchTextField(
            text = state.query,
            onValueChange = { viewModel.onEvent(TweetFeedEvent.OnQueryChange(it)) },
            onSearch = {
                keyboardController?.hide()
                viewModel.onEvent(TweetFeedEvent.GetTweetFeed)
            },
            onFocusChanged = {
                viewModel.onEvent(TweetFeedEvent.OnSearchFocusChange(it.isFocused))
            },
            shouldShowHint = state.isHintVisible
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.error.isNotEmpty()) {
            ErrorComponent(
                errorText = state.error,
                getTweets = { viewModel.onEvent(TweetFeedEvent.GetTweetFeed) })


        } else {
            LazyColumn {
                val tweets = state.tweets
                items(tweets.size) {
                    TweetCard(
                        title = tweets[it].tweetTag,
                        description = tweets[it].tweetText,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}