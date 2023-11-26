package com.lee.matchmate.main.geocoder

import com.google.gson.annotations.SerializedName


data class ReverseGeoEntity(
    val results: List<ReverseResult>,
    val status : String
)
data class ReverseResult(
    @SerializedName("formatted_address")
    val formattedAddress : String
)

