package com.lee.matchmate.main.geocoder

import com.google.firebase.firestore.Exclude

data class SpaceMarker(
    var lat: String = "",
    var lng: String = "",
){
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "lat" to lat,
            "lng" to lng
        )
    }
}

