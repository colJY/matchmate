package com.lee.matchmate.chat

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lee.matchmate.common.Constants
import com.lee.matchmate.main.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class ChatRepository {

    val _chatInfo = MutableStateFlow<User?>(null)

    fun getUserInfo(userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (userId.isNotEmpty()) {  // userId가 비어 있지 않은지 확인합니다.
                try {
                    FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION_NAME)
                        .document(userId)
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                return@addSnapshotListener
                            }

                            if (snapshot != null && snapshot.exists()) {
                                val user = snapshot.toObject(User::class.java)
                                _chatInfo.value = user
                            }
                        }
                } catch (e: Exception) {
                    Log.e(Constants.CHAT_REPO_TAG,e.toString())
                }
            }
        }
    }


}