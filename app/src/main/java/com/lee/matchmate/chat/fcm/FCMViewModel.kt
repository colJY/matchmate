package com.lee.matchmate.chat.fcm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.matchmate.common.RepositoryFactory
import kotlinx.coroutines.launch

class FCMViewModel: ViewModel() {
    private val repository = RepositoryFactory.fcmRepository

    fun sendPushNotification(notificationBody: NotificationBody){
        viewModelScope.launch {
            repository.sendPushNotification(notificationBody)
        }
    }
}