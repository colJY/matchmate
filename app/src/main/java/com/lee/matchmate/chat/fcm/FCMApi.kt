package com.lee.matchmate.chat.fcm

import com.lee.matchmate.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response

interface FCMApi {

    @POST("fcm/send")
    suspend fun sendPushNotification(
        @Body notification: NotificationBody
    ): Response<ResponseBody>
}