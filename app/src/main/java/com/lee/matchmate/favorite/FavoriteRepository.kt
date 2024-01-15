package com.lee.matchmate.favorite

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.lee.matchmate.common.Constants
import com.lee.matchmate.main.FireSpace
import com.lee.matchmate.main.NewSpace

class FavoriteRepository {
    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = Constants.FIRESTORE_COLLECTION_NAME
    private val documentRef = fireStoreDB.collection(fireStoreCollectionName)

    fun getFavoriteSpace(currentId: String, onSuccess: (List<NewSpace>) -> Unit) {
        documentRef
            .whereArrayContains(Constants.FAVORITE, currentId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val space = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FireSpace::class.java)?.let { NewSpace(doc.id, it) }
                }
                if (space != null) {
                    onSuccess(space)
                }
            }
    }

    fun toggleFavState(currentId: String, newSpace: NewSpace, onSuccess: (Boolean) -> Unit) {
        if (newSpace.space.fav.contains(currentId)) {
            newSpace.space.fav = newSpace.space.fav - currentId
        } else {
            newSpace.space.fav = newSpace.space.fav + currentId
        }
        documentRef.document(newSpace.id).update(Constants.FAVORITE, newSpace.space.fav)
            .addOnSuccessListener {
                onSuccess(true)
            }
            .addOnFailureListener { e ->
                onSuccess(false)
            }
    }
}