package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.gyadam.tweetfeedtestapp.R
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.components.SearchTextField
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.components.TweetCard
import hu.gyadam.tweetfeedtestapp.presentation.util.LocalSpacing

@Composable
fun TweetFeed(
    viewModel: TweetFeedViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .padding(spacing.spaceMedium)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.main_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        SearchTextField(
            text = "",
            onValueChange = {},
            onSearch = { /*TODO*/ },
            onFocusChanged = {},
            shouldShowHint = true
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