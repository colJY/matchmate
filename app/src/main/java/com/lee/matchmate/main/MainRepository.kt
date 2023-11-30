package com.lee.matchmate.main

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.lee.matchmate.main.geocoder.SpaceMarker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "Space"
    private val documentRef = fireStoreDB.collection(fireStoreCollectionName)

    private val fireStoreMarkerCollectionName = "latlng"
    private val documentMarkerRef = fireStoreDB.collection(fireStoreMarkerCollectionName)

    val spaceData = MutableLiveData<List<NewSpace>>()
    val newSpaceList = spaceData.value?.toMutableList() ?: mutableListOf()

    val spaceMarker = MutableLiveData<List<SpaceMarker>>()
    val mySpaceMarker = spaceMarker.value?.toMutableList() ?: mutableListOf()


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

    fun getMarkerData() {
        CoroutineScope(Dispatchers.IO).launch {
            documentMarkerRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    mySpaceMarker.clear()
                    for (doc in snapshot) {
                        val marker = doc.toObject(SpaceMarker::class.java)
                        mySpaceMarker.add(marker)
                    }
                    spaceMarker.postValue(mySpaceMarker.toList())

                } else {

                }
            }
        }
    }


    fun toggleFavState(newSpace: NewSpace, onComplete: (Boolean) -> Unit) {
        newSpace.space.fav = !newSpace.space.fav
        val docRef = fireStoreDB.collection(fireStoreCollectionName).document(newSpace.id)
        docRef.update("fav", newSpace.space.fav)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                onComplete(false)
            }
    }


}