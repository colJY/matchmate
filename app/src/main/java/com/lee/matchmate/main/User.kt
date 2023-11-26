package com.lee.matchmate.main

import com.google.firebase.firestore.Exclude

data class User(
    var userName : String = "",
    var profileImage : String = "",
    var spaceId : MutableList<String> = mutableListOf(),
    var userId : String = "",
) {
    @Exclude
    fun toMap() : Map<String, Any>{
        return mapOf(
            "userName" to userName,
            "profileImage" to profileImage,
            "spaceId" to spaceId,
            "userId" to userId,
        )
    }
}