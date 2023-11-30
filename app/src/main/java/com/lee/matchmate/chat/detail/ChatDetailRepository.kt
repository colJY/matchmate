package com.lee.matchmate.chat.detail

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.lee.matchmate.common.toastMessage
import com.lee.matchmate.main.FireSpace
import com.lee.matchmate.main.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatDetailRepository {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "chat"
    private val documentRef = fireStoreDB.collection(fireStoreCollectionName)
    val chatData = MutableLiveData<List<ChatMessage?>?>()
    val roomData = MutableLiveData<Chat?>()
    fun getChatData(documentId : String){
        CoroutineScope(Dispatchers.IO).launch {
            documentRef.document(documentId).addSnapshotListener{ snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if(snapshot != null){
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
            FirebaseFirestore.getInstance().collection("user").document(userId).get()
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