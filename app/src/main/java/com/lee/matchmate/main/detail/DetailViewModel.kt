package com.lee.matchmate.main.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.matchmate.common.RepositoryFactory
import com.lee.matchmate.main.FireSpace

class DetailViewModel : ViewModel() {

    private val repository = RepositoryFactory.detailRepository

    val detailSpaceData: MutableLiveData<FireSpace?> = repository.detailSpaceData

    fun setDetail(documentId: String) {
        repository.getSpaceDetail(documentId)
    }
    fun addChatIdToUser(chatRoomId: String, userId: String) {
        repository.addChatIdToUser(chatRoomId, userId)
    }
}