package com.lee.matchmate.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    companion object {
        fun createClient(interceptor : HttpLoggingInterceptor, headers : Map<String,String>) : OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val newRequest : Request = request.newBuilder()
                        .apply {
                            headers.forEach{
                                addHeader(it.key,it.value)
                            }
                        }
                        .build()
                    chain.proceed((newRequest))

                }
                .addInterceptor(interceptor)
                .build()
        }

        fun createRetrofit(client: OkHttpClient, url : String) : Retrofit{
            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

    }
}

