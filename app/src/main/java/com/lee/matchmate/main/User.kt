package com.lee.matchmate.main

import com.google.firebase.firestore.Exclude
import com.lee.matchmate.common.Constants

data class User(
    var userName : String = Constants.BLANK,
    var profileImage : String = Constants.BLANK,
    var spaceId : MutableList<String> = mutableListOf(),
    var userId : String = Constants.BLANK,
    var chatId : MutableList<String> = mutableListOf(),
    var fcmToken : String = Constants.BLANK
) {
    @Exclude
    fun toMap() : Map<String, Any>{
        return mapOf(
            Constants.FIELD_USER_NAME to userName,
            Constants.FIELD_PROFILE_IMAGE to profileImage,
            Constants.FIELD_SPACE_ID to spaceId,
            Constants.FIELD_USER_ID to userId,
            Constants.FIELD_CHAT_ID to chatId,
            Constants.FIELD_FCM_TOKEN to fcmToken,
        )
    }
}