package com.lee.matchmate.chat.fcm

import com.lee.matchmate.BuildConfig
import com.lee.matchmate.common.Constants
import com.lee.matchmate.common.RetrofitFactory
import okhttp3.logging.HttpLoggingInterceptor

class FCMRepository {

    private lateinit var retrofit: FCMApi


    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        val headers = mapOf(
            Constants.HEADER_AUTHORIZATION to (Constants.AUTHORIZATION_KEY_PREFIX + BuildConfig.FCM_SERVER_KEY),
            Constants.HEADER_CONTENT_TYPE to Constants.CONTENT_TYPE
        )
        val client = RetrofitFactory.createClient(interceptor, headers)

        val retrofitInstance = RetrofitFactory.createRetrofit(client, Constants.BASE_URL)
        retrofit = retrofitInstance.create(FCMApi::class.java)
    }

    suspend fun sendPushNotification(notificationBody: NotificationBody) {
        retrofit.sendPushNotification(notificationBody)
    }
}
