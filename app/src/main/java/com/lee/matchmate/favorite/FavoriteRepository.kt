package com.lee.matchmate.favorite

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.lee.matchmate.main.FireSpace
import com.lee.matchmate.main.NewSpace

class FavoriteRepository {
    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "Space"
    private val documentRef = fireStoreDB.collection(fireStoreCollectionName)


    fun getFavoriteSpace(currentId: String, onSuccess: (List<NewSpace>) -> Unit){
        documentRef
            .whereEqualTo("userId", currentId)
            .whereEqualTo("fav",true)
            .addSnapshotListener { snapshot, e ->
                if(e != null){
                    Log.e("FavoriteRepository",e.toString())
                    return@addSnapshotListener
                }
                val space = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FireSpace::class.java)?.let { NewSpace(doc.id, it) }
                }
                if (space != null){
                    Log.d("FavoriteRepository", "Fetched spaces: $space")
                    onSuccess(space)
                }
            }
    }

    fun toggleFavState(newSpace: NewSpace, onSuccess: (Boolean) -> Unit){
        newSpace.space.fav = !newSpace.space.fav
        documentRef.document(newSpace.id).update("fav", newSpace.space.fav)
            .addOnSuccessListener {
                onSuccess(true)
            }
            .addOnFailureListener{e->
                onSuccess(false)
            }

    }
}