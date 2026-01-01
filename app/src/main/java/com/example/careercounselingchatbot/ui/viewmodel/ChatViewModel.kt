package com.example.careercounselingchatbot.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.careercounselingchatbot.data.model.Message
import com.example.careercounselingchatbot.data.repository.ChatRepository
import com.example.careercounselingchatbot.utils.Constants
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        addWelcomeMessage()
    }

    private fun addWelcomeMessage() {
        val welcomeMessage = Message(
            text = Constants.WELCOME_MESSAGE,
            isUser = false
        )
        repository.addMessage(welcomeMessage)
        _messages.value = repository.getMessages()
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = Message(text = text.trim(), isUser = true)
        repository.addMessage(userMessage)
        _messages.value = repository.getMessages()

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.sendMessageToAI(text.trim())

            result.onSuccess { response ->
                val aiMessage = Message(text = response, isUser = false)
                repository.addMessage(aiMessage)
                _messages.value = repository.getMessages()
            }.onFailure { exception ->
                _error.value = "Failed to get response: ${exception.message}"

                val errorMessage = Message(
                    text = "Sorry, I encountered an error. Please check your internet connection and try again.",
                    isUser = false
                )
                repository.addMessage(errorMessage)
                _messages.value = repository.getMessages()
            }

            _isLoading.value = false
        }
    }

    fun clearChat() {
        repository.clearChat()
        addWelcomeMessage()
    }

    fun clearError() {
        _error.value = null
    }
}