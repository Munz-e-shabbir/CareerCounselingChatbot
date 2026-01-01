package com.example.careercounselingchatbot.data.repository

import com.example.careercounselingchatbot.data.model.Message
import com.example.careercounselingchatbot.network.GeminiApiService

class ChatRepository {

    private val apiService = GeminiApiService()
    private val messages = mutableListOf<Message>()

    fun getMessages(): List<Message> = messages.toList()

    fun addMessage(message: Message) {
        messages.add(message)
    }

    suspend fun sendMessageToAI(userMessage: String): Result<String> {
        return apiService.sendMessage(userMessage)
    }

    fun clearChat() {
        messages.clear()
        apiService.clearHistory()
    }
}