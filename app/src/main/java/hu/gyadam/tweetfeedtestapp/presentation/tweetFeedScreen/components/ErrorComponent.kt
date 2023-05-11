package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hu.gyadam.tweetfeedtestapp.R

@Composable
fun ErrorComponent(
    modifier : Modifier = Modifier,
    errorText :String,
    getTweets :()->Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Error : ${errorText}")
        Button(onClick =  getTweets ) {
            Text(text = stringResource(id = R.string.retry_button_text))
        }
    }
}