package com.panther.contentai.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panther.contentai.models.Chat

class ChatViewModel : ViewModel() {
    private val _chatMessages = MutableLiveData<List<Chat>>()
    val chatMessages: LiveData<List<Chat>> = _chatMessages

    init {
        _chatMessages.value = emptyList()
    }


    fun addMessage(message: Chat) {
        val currentMessages = _chatMessages.value ?: emptyList()
        val newMessages = currentMessages.toMutableList()
        newMessages.add(message)
        _chatMessages.value = newMessages
    }
}