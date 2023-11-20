package com.lee.matchmate.main.selectchip


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ChipSelectViewModel() : ViewModel() {
    private val repository: ChipSelectRepository = ChipSelectRepository()

    val chipData: LiveData<String> = repository.chipData

    init {
        repository.getChipData()
    }
}
