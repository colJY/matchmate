package com.lee.matchmate.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.matchmate.main.geocoder.SpaceMarker

class MainViewModel : ViewModel() {
    private val repository : MainRepository = MainRepository()

    val spaceData : LiveData<List<NewSpace>> = repository.spaceData
    val spaceMarker : LiveData<List<SpaceMarker>> = repository.spaceMarker

    val selectedCity = MutableLiveData<String>()
    val selectedDistrict = MutableLiveData<String>()



    init {
        repository.getSpaceData()
        repository.getMarkerData()
    }
}