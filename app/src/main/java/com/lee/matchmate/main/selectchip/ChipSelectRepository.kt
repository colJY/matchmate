package com.lee.matchmate.main.selectchip

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lee.matchmate.common.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChipSelectRepository() {


    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = Constants.COND_COLLECTION_NAME
    private val fireStoreDocumentName = Constants.COND_DOCUMENT_NAME
    private val fireStoreFieldName = Constants.COND_FIELD_NAME

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
                }
            }
        }

    }
}