package hu.gyadam.tweetfeedtestapp.data.remote.dto

data class TweetModel(
    val `data`: Data,
    val matching_rules: List<MatchingRule>
)