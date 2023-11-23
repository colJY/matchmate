package com.lee.matchmate.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val repository : MainRepository = MainRepository()

    val spaceData : LiveData<List<NewSpace>> = repository.spaceData

    val selectedCity = MutableLiveData<String>()
    val selectedDistrict = MutableLiveData<String>()



    init {
        repository.getSpaceData()
    }
}