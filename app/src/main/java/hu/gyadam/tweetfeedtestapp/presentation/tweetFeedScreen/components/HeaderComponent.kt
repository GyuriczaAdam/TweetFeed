package hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import hu.gyadam.tweetfeedtestapp.R
import hu.gyadam.tweetfeedtestapp.domain.observer.ConnectivityObserver
import hu.gyadam.tweetfeedtestapp.presentation.util.LocalSpacing

@Composable
fun HeaderComponent(
     status : String
) {
    val spacing = LocalSpacing.current
    Text(
        text = stringResource(id = R.string.main_title),
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Start
    )
    Spacer(modifier = androidx.compose.ui.Modifier.height(spacing.spaceMedium))
    Row {
        Text(
            text = stringResource(id = R.string.connection_status_text),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Start
        )
        Text(
            text = status,
            style = MaterialTheme.typography.headlineMedium.copy(
                if (status.equals(ConnectivityObserver.Status.Available.name)) Color.Green else Color.Red,
            ),
            textAlign = TextAlign.Start
        )
    }
}