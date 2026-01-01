package com.example.careercounselingchatbot.network

import com.google.ai.client.generativeai.GenerativeModel
import com.example.careercounselingchatbot.BuildConfig
import com.example.careercounselingchatbot.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiApiService {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val chatHistory = mutableListOf<Pair<String, String>>()

    suspend fun sendMessage(userMessage: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Build conversation context
                val fullPrompt = buildPrompt(userMessage)

                val response = generativeModel.generateContent(fullPrompt)
                val responseText = response.text ?: "I apologize, but I couldn't generate a response. Please try again."

                // Store in history
                chatHistory.add(Pair(userMessage, responseText))

                Result.success(responseText)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun buildPrompt(userMessage: String): String {
        val context = StringBuilder()
        context.append(Constants.SYSTEM_PROMPT)
        context.append("\n\n")

        // Add recent conversation history (last 5 exchanges)
        val recentHistory = chatHistory.takeLast(5)
        if (recentHistory.isNotEmpty()) {
            context.append("Previous conversation:\n")
            recentHistory.forEach { (user, assistant) ->
                context.append("User: $user\n")
                context.append("Assistant: $assistant\n\n")
            }
        }

        context.append("Current User Question: $userMessage\n")
        context.append("Assistant:")

        return context.toString()
    }

    fun clearHistory() {
        chatHistory.clear()
    }
}