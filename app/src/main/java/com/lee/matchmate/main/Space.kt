package com.lee.matchmate.main

import com.google.firebase.firestore.Exclude


data class NewSpace(val id: String, val space: FireSpace)

data class FireSpace(
    var location: String = "",
    var primaryImage: String = "",
    var additionalImage: String = "",
    var cond: String = "",
    var value: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "location" to location,
            "primaryImage" to primaryImage,
            "additionalImage" to additionalImage,
            "cond" to cond,
            "value" to value,
        )
    }
}

data class Space(val name: String, val value: String, val location: String)

