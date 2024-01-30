package com.lee.matchmate.main.geocoder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lee.matchmate.common.RepositoryFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeocoderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RepositoryFactory.geocoderRepository

    fun getGeoEntityResponseLiveData(): LiveData<GeoEntity> {
        return repository.getGeoEntityResponseLiveData()
    }

    fun getGeoCode(components: String, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getGeoCode(components, apiKey)
        }
    }

    fun getReverseGeoEntityResponseLiveData(): LiveData<ReverseGeoEntity> {
        return repository.getReverseEntityResponseLiveData()
    }

    fun getReverseGeoCode(latlng: String, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getReverseGeoCode(latlng, apiKey)
        }
    }

}