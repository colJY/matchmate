package com.lee.matchmate.main

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.lee.matchmate.common.Constants
import com.lee.matchmate.main.geocoder.SpaceMarker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = Constants.FIRESTORE_COLLECTION_NAME
    private val documentRef = fireStoreDB.collection(fireStoreCollectionName)

    private val fireStoreMarkerCollectionName = Constants.LATLNG_COLLECTION_NAME
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

                }
            }
        }
    }


    fun toggleFavState(currentId: String, newSpace: NewSpace, onComplete: (Boolean) -> Unit) {
        if (newSpace.space.fav.contains(currentId)) {
            newSpace.space.fav = newSpace.space.fav - currentId
        } else {
            newSpace.space.fav = newSpace.space.fav + currentId
        }
        val docRef = fireStoreDB.collection(fireStoreCollectionName).document(newSpace.id)
        docRef.update(Constants.FAVORITE, newSpace.space.fav)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                onComplete(false)
            }
    }


}