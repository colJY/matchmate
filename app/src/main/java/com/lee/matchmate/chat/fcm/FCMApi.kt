package com.lee.matchmate.chat.fcm

import com.lee.matchmate.common.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {
    @POST(Constants.FCM_SEND_ENDPOINT)
    suspend fun sendPushNotification(
        @Body notification: NotificationBody
    ): Response<ResponseBody>
}


