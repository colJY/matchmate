package com.lee.matchmate.chat.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.matchmate.main.User

class ChatDetailViewModel : ViewModel() {
    private val repository = ChatDetailRepository()

    val chatMessages : MutableLiveData<List<ChatMessage?>?> = repository.chatData
    val roomData : MutableLiveData<Chat?> = repository.roomData
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> get() = _userInfo
    private val userCache = mutableMapOf<String, User?>()



    fun getChatData(documentId: String) {
        repository.getChatData(documentId)
    }

    fun insertChatToFireStore(chat: Chat, roomId: String) {
        repository.insertFireStore(chat, roomId)
    }

    fun getUserInfo(userId: String, onComplete: (User?) -> Unit){
        if (userCache.containsKey(userId)) {
            _userInfo.postValue(userCache[userId])
            onComplete(userCache[userId])
        } else {
            repository.getUserInfo(userId) { user ->
                userCache[userId] = user
                _userInfo.postValue(user)
                onComplete(user)
            }
        }
    }


    fun getCachedUserInfo(userId: String): User? {
        return userCache[userId]
    }
}