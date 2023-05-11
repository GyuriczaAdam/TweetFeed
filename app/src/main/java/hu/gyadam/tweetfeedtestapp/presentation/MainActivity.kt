package hu.gyadam.tweetfeedtestapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.gyadam.tweetfeedtestapp.presentation.tweetFeedScreen.TweetFeed
import hu.gyadam.tweetfeedtestapp.presentation.ui.theme.TweetFeedTestAppTheme
import hu.gyadam.tweetfeedtestapp.presentation.util.Screens
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TweetFeedTestAppTheme {
                val navController = rememberNavController()
                navHost(navController = navController)
            }
        }
    }

    @Composable
    fun navHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Screens.TweetFeedScreen.route
        ) {
            composable(route = Screens.TweetFeedScreen.route) {
                TweetFeed()
            }
        }
    }
}
