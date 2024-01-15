package com.lee.matchmate.chat.detail

import com.google.firebase.firestore.Exclude
import com.lee.matchmate.common.Constants


data class Chat(
    val id: String = "",
    val selectedCondList: List<String> = listOf(),
    val chatMessage: List<ChatMessage> = listOf(),
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            Constants.FIELD_ID to id,
            Constants.FIELD_SELECTED_COND_LIST to selectedCondList,
            Constants.FIELD_CHAT_MESSAGE to chatMessage.map { it.toMap() },
        )
    }
}

data class ChatMessage(
    val sender: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            Constants.FIELD_SENDER to sender,
            Constants.FIELD_TIMESTAMP to timestamp,
            Constants.FIELD_MESSAGE to message,
        )
    }
}


