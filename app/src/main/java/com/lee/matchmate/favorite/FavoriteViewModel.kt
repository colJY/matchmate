package com.lee.matchmate.favorite

import androidx.lifecycle.ViewModel
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.main.NewSpace

class FavoriteViewModel : ViewModel() {
    private val repository = FavoriteRepository()


    fun getFavoriteSpace(currentId : String, onSuccess : (List<NewSpace>) -> Unit){
        repository.getFavoriteSpace(currentId, onSuccess)
    }

    fun toggleFavState(newSpace: NewSpace, onComplete: (Boolean) -> Unit) {
        repository.toggleFavState(newSpace, onComplete)
    }



}