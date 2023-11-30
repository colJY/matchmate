package com.lee.matchmate.chat.detail

import com.google.firebase.firestore.Exclude


data class Chat(
    val id: String = "",
    val selectedCondList: List<String> = listOf(),
    val chatMessage: List<ChatMessage> = listOf(),
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "selectedCondList" to selectedCondList,
            "chatMessage" to chatMessage.map { it.toMap() },
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
            "sender" to sender,
            "timestamp" to timestamp,
            "message" to message,
        )
    }
}


