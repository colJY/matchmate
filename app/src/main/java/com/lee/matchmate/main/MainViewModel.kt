package com.lee.matchmate.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.matchmate.main.geocoder.SpaceMarker
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    private val repository: MainRepository = MainRepository()

    val spaceData: LiveData<List<NewSpace>> = repository.spaceData
    val spaceMarker: LiveData<List<SpaceMarker>> = repository.spaceMarker

    val selectedCity = MutableLiveData<String>()
    val selectedDistrict = MutableLiveData<String>()

    val selectedType = MutableLiveData<String>()
    val selectedMinValue = MutableLiveData<String>()
    val selectedMaxValue = MutableLiveData<String>()
    val selectedCondList = MutableLiveData<List<String>>()

    val filteredData = MutableLiveData<List<NewSpace>?>()

    var isSuccess: MutableStateFlow<Boolean> = MutableStateFlow(true)

    fun filterData() {
        val minValue = selectedMinValue.value?.toFloatOrNull() ?: 0f
        val maxValue = selectedMaxValue.value?.let {
            val value = it.toFloatOrNull()
            if (value == 0f || value == null) 200f else value
        } ?: 200f

        val type = selectedType.value
        val cond = selectedCondList.value
        val district = selectedDistrict.value

        val filteredList = spaceData.value?.filter {
            (type.isNullOrEmpty() || it.space.type == type) &&
                    it.space.value.toFloat() in minValue..maxValue &&
                    (cond.isNullOrEmpty() || cond.any { condItem -> it.space.cond.contains(condItem) }) &&
                    (district.isNullOrEmpty() || it.space.location.contains(district))

        }

        if (filteredList != null) {
            filteredData.value = filteredList
        } else {
            filteredData.value = spaceData.value
        }

    }


    fun toggleFavState(currentId: String, newSpace: NewSpace, onComplete: (Boolean) -> Unit) {
        repository.toggleFavState(currentId, newSpace, onComplete)
    }


    init {
        repository.getSpaceData()
        repository.getMarkerData()
    }
}

