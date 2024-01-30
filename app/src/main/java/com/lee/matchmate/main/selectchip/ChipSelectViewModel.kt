package com.lee.matchmate.main.selectchip


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lee.matchmate.common.RepositoryFactory

class ChipSelectViewModel() : ViewModel() {
    private val repository = RepositoryFactory.chipSelectRepository

    val chipData: LiveData<String> = repository.chipData

    init {
        repository.getChipData()
    }
}
