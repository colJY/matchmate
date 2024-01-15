package com.lee.matchmate.main.geocoder

import com.google.gson.annotations.SerializedName
import com.lee.matchmate.common.Constants


data class ReverseGeoEntity(
    val results: List<ReverseResult>,
    val status : String
)
data class ReverseResult(
    @SerializedName(Constants.FormattedAddress)
    val formattedAddress : String
)

