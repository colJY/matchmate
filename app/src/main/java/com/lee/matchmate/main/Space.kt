package com.lee.matchmate.main

import com.google.firebase.firestore.Exclude


data class NewSpace(val id: String, val space: FireSpace)

data class FireSpace(
    var location: String = "",
    var primaryImage: String = "",
    var additionalImage: String = "",
    var cond: String = "",
    var value: String = "",
    var type : String = "",
    var userId : String = "",
    var title : String = "제목 없음",
    var fav : Boolean = false,
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "location" to location,
            "primaryImage" to primaryImage,
            "additionalImage" to additionalImage,
            "cond" to cond,
            "value" to value,
            "type" to type,
            "userId" to userId,
            "title" to title,
            "fav" to fav,
        )
    }
}

