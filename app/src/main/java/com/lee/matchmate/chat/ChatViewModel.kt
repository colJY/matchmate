package com.lee.matchmate.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.matchmate.main.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//@HiltViewModel

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    val chatInfo: StateFlow<User?> get() = repository._chatInfo
    fun chatUserInfo(userId: String) {
        viewModelScope.launch {
            repository.getUserInfo(userId)
        }
    }

}


