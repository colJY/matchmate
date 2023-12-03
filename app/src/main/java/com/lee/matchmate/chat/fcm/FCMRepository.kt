package com.lee.matchmate.chat.fcm

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lee.matchmate.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class FCMRepository {

    companion object {
        private const val BASE_URL = "https://fcm.googleapis.com/"
    }

    private lateinit var retrofit: FCMApi

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val newRequest: Request = request.newBuilder()
                    .addHeader("Authorization", "key=${BuildConfig.FCM_SERVER_KEY}")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)

            }
            .addInterceptor(interceptor)
            .build()

        val gson: Gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FCMApi::class.java)
    }

    suspend fun sendPushNotification(notificationBody: NotificationBody) {
        retrofit.sendPushNotification(notificationBody)
    }
}
