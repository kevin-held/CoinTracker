package com.example.cointracker.model

/*
Class representing JSON response for getToken()
 */
data class Token(
    val access_token: String,
    val scope: String,
    val expires_in: Int,
    val token_type: String
)
