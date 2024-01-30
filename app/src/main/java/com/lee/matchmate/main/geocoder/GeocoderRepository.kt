package com.lee.matchmate.main.geocoder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lee.matchmate.common.Constants
import com.lee.matchmate.common.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * GeocoderRepository는 Geocoding API를 사용하여 위치 데이터를 처리하는 클래스입니다.
 * 해당 클래스는 주소를 좌표로 변환(getGeoCode)하거나, 좌표를 주소로 변환(getReverseGeoCode)하는 기능을 제공합니다.
 */
class GeocoderRepository {


    private var geocoderApi: GeocoderApi
    private var geoEntityMutableLiveData: MutableLiveData<GeoEntity> = MutableLiveData()
    private var reverseGeoEntityLiveData: MutableLiveData<ReverseGeoEntity> = MutableLiveData()

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = RetrofitFactory.createClient(interceptor, mapOf())
        val retrofit = RetrofitFactory.createRetrofit(client, Constants.MAP_BASE_URL)
        geocoderApi = retrofit.create(GeocoderApi::class.java)
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