package com.lee.matchmate.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.matchmate.main.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository : ChatRepository) : ViewModel() {

    val chatInfo: StateFlow<User?> get() = repository._chatInfo
    fun chatUserInfo(userId: String) {
        viewModelScope.launch {
            repository.getUserInfo(userId)
        }
    }

}


