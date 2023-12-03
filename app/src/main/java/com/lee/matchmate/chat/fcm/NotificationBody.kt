package com.lee.matchmate.chat.fcm

data class NotificationBody(val to : String, val data : NotificationData)

data class NotificationData(
    val title : String,
    val message : String,
)