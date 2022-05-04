package com.example.cointracker.model


/*
RequestBody for POST call to getToken()
client_id is required
 */
data class RequestBody(
    var audience: String = "https://api.bravenewcoin.com",
    var client_id: String,
    var grant_type: String = "client_credentials"
)
