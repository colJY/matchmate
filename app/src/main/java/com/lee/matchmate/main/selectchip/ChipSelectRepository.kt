package com.lee.matchmate.main.selectchip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChipSelectRepository() {

    private val job = CoroutineScope(Dispatchers.Main)

    fun getChipData(): LiveData<List<String>> {
        val liveChipData = MutableLiveData<List<String>>()
        job.launch {
            liveChipData.value = listOf("infp", "estj", "enfp", "운동", "요리", "자전거", "테니스")
        }
        return liveChipData
    }
}