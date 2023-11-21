package com.lee.matchmate.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "Space"
    private val documentRef = fireStoreDB.collection(fireStoreCollectionName)

    val spaceData = MutableLiveData<List<NewSpace>>()
    val newSpaceList = spaceData.value?.toMutableList() ?: mutableListOf()


    fun getSpaceData() {
        CoroutineScope(Dispatchers.IO).launch {
            documentRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    newSpaceList.clear()
                    for (doc in snapshot) {
                        val id = doc.id
                        val fireSpace = doc.toObject(FireSpace::class.java)
                        val newSpace = NewSpace(id, fireSpace)
                        newSpaceList.add(newSpace)
                    }
                    spaceData.postValue(newSpaceList.toList())

                } else {

                }
            }
        }

    }



}