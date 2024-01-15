package com.lee.matchmate.main

import com.google.firebase.firestore.Exclude
import com.lee.matchmate.common.Constants


data class NewSpace(val id: String, val space: FireSpace)

data class FireSpace(
    var location: String = Constants.BLANK,
    var primaryImage: String = Constants.BLANK,
    var additionalImage: String = Constants.BLANK,
    var cond: String = Constants.BLANK,
    var value: String = Constants.BLANK,
    var type : String = Constants.BLANK,
    var userId : String = Constants.BLANK,
    var title : String = Constants.NONE_TITLE,
    var fav : List<String> = listOf(),
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            Constants.FIELD_LOCATION to location,
            Constants.FIELD_PRIMARY_IMAGE to primaryImage,
            Constants.FIELD_ADDITIONAL_IMAGE to additionalImage,
            Constants.FIELD_COND to cond,
            Constants.FIELD_VALUE to value,
            Constants.FIELD_TYPE to type,
            Constants.FIELD_USER_ID to userId,
            Constants.FIELD_TITLE to title,
            Constants.FIELD_FAV to fav,
        )
    }
}

