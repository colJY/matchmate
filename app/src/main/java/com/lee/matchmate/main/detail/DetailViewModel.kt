package com.lee.matchmate.main.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.matchmate.main.FireSpace

class DetailViewModel : ViewModel() {

    private val repository: DetailRepository = DetailRepository()

    val detailSpaceData: MutableLiveData<FireSpace?> = repository.detailSpaceData

    fun setDetail(documentId: String) {
        repository.getSpaceDetail(documentId)
    }
}