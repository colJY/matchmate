package com.lee.matchmate.main.geocoder

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocoderApi {
    @GET("maps/api/geocode/json")
    suspend fun getGeoCode(
        @Query("components") components : String,
        @Query("key") apiKey : String
    ) : Response<GeoEntity>

    @GET("maps/api/geocode/json")
    suspend fun getReverseGeoCode(
        @Query("latlng") latlng : String,
        @Query("key") apiKey: String,
        @Query("language") language: String = "ko"
    ) : Response<ReverseGeoEntity>
}