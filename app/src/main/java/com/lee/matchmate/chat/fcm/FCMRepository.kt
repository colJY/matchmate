package com.lee.matchmate.chat.fcm

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lee.matchmate.BuildConfig
import com.lee.matchmate.common.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FCMRepository {

    private lateinit var retrofit: FCMApi

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest: Request = request.newBuilder()
                    .addHeader(
                        Constants.HEADER_AUTHORIZATION,
                        Constants.AUTHORIZATION_KEY_PREFIX + BuildConfig.FCM_SERVER_KEY
                    )
                    .addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE)
                    .build()
                chain.proceed(newRequest)

            }
            .addInterceptor(interceptor)
            .build()

        val gson: Gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FCMApi::class.java)
    }

    suspend fun sendPushNotification(notificationBody: NotificationBody) {
        retrofit.sendPushNotification(notificationBody)
    }
}
