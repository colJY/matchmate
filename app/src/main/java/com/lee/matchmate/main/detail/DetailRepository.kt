package com.lee.matchmate.main.detail

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.lee.matchmate.main.FireSpace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailRepository {
    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "Space"
    private val documentRef = fireStoreDB.collection(fireStoreCollectionName)

    val detailSpaceData = MutableLiveData<FireSpace?>()


    fun getSpaceDetail(documentId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            documentRef.document(documentId).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val fireDetail = snapshot.toObject(FireSpace::class.java)
                    detailSpaceData.postValue(fireDetail)
                } else {

                }

            }
        }
    }
}