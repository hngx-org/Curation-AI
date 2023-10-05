package com.panther.contentai.models

data class Chat(
    val sessionId: String? = null,
    val name: String? = null,
    val userMessage: String? = null,
    val aiMessage: String? = null,
)
