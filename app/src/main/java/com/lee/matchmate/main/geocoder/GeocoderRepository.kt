package com.lee.matchmate.main.geocoder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lee.matchmate.common.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeocoderRepository {


    private var geocoderApi: GeocoderApi
    private var geoEntityMutableLiveData: MutableLiveData<GeoEntity> = MutableLiveData()
    private var reverseGeoEntityLiveData: MutableLiveData<ReverseGeoEntity> = MutableLiveData()

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson: Gson = GsonBuilder().setLenient().create()

        geocoderApi = Retrofit.Builder()
            .baseUrl(Constants.MAP_BASE_URL)
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

            }
        } catch (e: Exception) {
            Log.e(Constants.GEO_TAG, e.toString())
        }
    }


    fun getReverseEntityResponseLiveData(): LiveData<ReverseGeoEntity> {
        return this.reverseGeoEntityLiveData
    }
}