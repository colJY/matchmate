package com.lee.matchmate.chat.detail

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.lee.matchmate.common.Constants
import com.lee.matchmate.main.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatDetailRepository {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = Constants.CHAT_COLLECTION_NAME
    private val documentRef = fireStoreDB.collection(fireStoreCollectionName)
    val chatData = MutableLiveData<List<ChatMessage?>?>()
    val roomData = MutableLiveData<Chat?>()

    fun getChatData(documentId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            documentRef.document(documentId).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val chat = snapshot.toObject(Chat::class.java)

                    chatData.postValue(chat?.chatMessage ?: listOf())
                    roomData.postValue(chat)
                }

            }
        }
    }

    fun insertFireStore(chat: Chat, roomId: String) {
        val documentRef = fireStoreDB.collection(fireStoreCollectionName).document(roomId)

        documentRef.get().addOnSuccessListener {
            if (it.exists()) {
                documentRef.set(chat.toMap())
            } else {
                documentRef.set(chat.toMap()).addOnSuccessListener {

                }.addOnFailureListener {

                }
            }
        }
    }

    fun getUserInfo(userId: String, onComplete: (User?) -> Unit) {
        if (userId.isNotEmpty()) {  // userId가 비어 있지 않은지 확인합니다.
            FirebaseFirestore.getInstance().collection(Constants.USER_COLLECTION_NAME)
                .document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(User::class.java)
                        onComplete(user)
                    }
                }
                .addOnFailureListener {
                    onComplete(null)
                }
        } else {
            onComplete(null)
        }
    }

}