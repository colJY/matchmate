package com.lee.matchmate.main.geocoder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lee.matchmate.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.math.ln

class GeocoderRepository {

    companion object {
        private const val BASE_URL = "https://maps.googleapis.com/"
    }

    private lateinit var geocoderApi: GeocoderApi
    private var geoEntityMutableLiveData: MutableLiveData<GeoEntity> = MutableLiveData()
    private var reverseGeoEntityLiveData: MutableLiveData<ReverseGeoEntity> = MutableLiveData()

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder()
            /*.addInterceptor { chain ->
                val request = chain.request()
                val newRequest: Request = request.newBuilder()
                    .addHeader("apiKey", BuildConfig.MAPS_API_KEY)
                    .build()
                chain.proceed(newRequest)

            }*/
            .addInterceptor(interceptor)
            .build()

        val gson: Gson = GsonBuilder().setLenient().create()

        geocoderApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GeocoderApi::class.java)
    }

    suspend fun getGeoCode(components: String, apiKey: String) {
        val response = withContext(Dispatchers.IO) {
            geocoderApi.getGeoCode(components, apiKey)
        }
        if (response.isSuccessful) {
            geoEntityMutableLiveData.postValue(response.body())
        } else {

        }
    }

    fun getGeoEntityResponseLiveData(): LiveData<GeoEntity> {
        return this.geoEntityMutableLiveData
    }

    suspend fun getReverseGeoCode(latlng: String, apiKey: String) {
        try {
            val response = withContext(Dispatchers.IO) {
                geocoderApi.getReverseGeoCode(latlng, apiKey)
            }
            if (response.isSuccessful) {
                reverseGeoEntityLiveData.postValue(response.body())
//                Log.e("asd", "${response.body()}")

            } else {

            }
        } catch (e: Exception) {
            Log.e("asd", "예외 발생: ${e.message}")
        }
    }


    fun getReverseEntityResponseLiveData(): LiveData<ReverseGeoEntity> {
        return this.reverseGeoEntityLiveData
    }
}