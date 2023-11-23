package com.lee.matchmate.main.geocoder

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lee.matchmate.main.selectchip.ChipSelectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeocoderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GeocoderRepository = GeocoderRepository()

    fun getGeoEntityResponseLiveData(): LiveData<GeoEntity> {
        return repository.getGeoEntityResponseLiveData()
    }

    fun getGeoCode(components: String, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getGeoCode(components, apiKey)
        }
    }

    fun getReverseGeoEntityResponseLiveData() : LiveData<ReverseGeoEntity>{
        return repository.getReverseEntityResponseLiveData()
    }

    fun getReverseGeoCode(latlng: String, apiKey: String){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getReverseGeoCode(latlng, apiKey)
        }
    }

}