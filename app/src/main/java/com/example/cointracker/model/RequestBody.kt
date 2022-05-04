package com.example.cointracker.model

data class RequestBody(
    var audience: String = "https://api.bravenewcoin.com",
    var client_id: String = "oCdQoZoI96ERE9HY3sQ7JmbACfBf55RY",
    var grant_type: String = "client_credentials"
)
