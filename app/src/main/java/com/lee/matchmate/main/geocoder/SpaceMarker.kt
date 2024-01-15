package com.lee.matchmate.main.geocoder

import com.google.firebase.firestore.Exclude
import com.lee.matchmate.common.Constants

data class SpaceMarker(
    var lat: String = Constants.BLANK,
    var lng: String = Constants.BLANK,
){
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            Constants.lat to lat,
            Constants.lng to lng
        )
    }
}

