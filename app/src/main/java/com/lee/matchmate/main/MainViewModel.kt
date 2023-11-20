package com.lee.matchmate.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val repository : MainRepository = MainRepository()

    val spaceData : LiveData<List<NewSpace>> = repository.spaceData

    init {
        repository.getSpaceData()
    }
}