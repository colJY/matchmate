package com.lee.matchmate.chat.fcm

import android.app.Application
import android.app.Notification
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FCMViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : FCMRepository = FCMRepository()

    fun sendPushNotification(notificationBody: NotificationBody){
        viewModelScope.launch {
            repository.sendPushNotification(notificationBody)
        }
    }
}