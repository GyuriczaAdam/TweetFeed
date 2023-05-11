package hu.gyadam.tweetfeedtestapp.domain.observer

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observer(context: Context) : Flow<Status>

    enum class  Status {
        Available,Unavailable,Losing,Lost
    }
}