package com.lee.matchmate.main.geocoder

import com.lee.matchmate.common.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

    interface GeocoderApi {
        @GET(Constants.GEOCODE_API_URL)
        suspend fun getGeoCode(
            @Query(Constants.Components) components : String,
            @Query(Constants.Key) apiKey : String
        ) : Response<GeoEntity>

        @GET(Constants.GEOCODE_API_URL)
        suspend fun getReverseGeoCode(
            @Query(Constants.LATLNG_COLLECTION_NAME) latlng : String,
            @Query(Constants.Key) apiKey: String,
            @Query(Constants.Language) language: String = Constants.DEFAULT_LANGUAGE
        ) : Response<ReverseGeoEntity>
    }


