package com.lee.matchmate.main.selectchip

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChipSelectRepository() {


    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "cond"
    private val fireStoreDocumentName = "condition"
    private val fireStoreFieldName = "condList"

    val chipData = MutableLiveData<String>()
    fun getChipData() {
        CoroutineScope(Dispatchers.IO).launch {
            val documentRef = fireStoreDB.collection(fireStoreCollectionName).document(fireStoreDocumentName)
            documentRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    chipData.postValue(snapshot.getString(fireStoreFieldName))
                } else {

                }
            }
        }

    }
}