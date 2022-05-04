package com.example.cointracker.api

import com.example.cointracker.BuildConfig


data class Credentials (
    var apiKey: String = BuildConfig.API_KEY,
    var clientId: String = BuildConfig.CLIENT_ID,
    var bearer: String = ""
)