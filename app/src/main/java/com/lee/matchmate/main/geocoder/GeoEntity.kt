package com.lee.matchmate.main.geocoder

import com.google.android.gms.maps.model.LatLng

data class GeoEntity(
    val results: List<Result>,
    val status : String
)

data class Result(
    val geometry : Geometry
)

data class Geometry (
    val location : Location
)

data class Location(
    val lat : Double,
    val lng : Double
)

