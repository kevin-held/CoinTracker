package com.example.cointracker.api

import com.example.cointracker.BuildConfig

/*
Class containing authorizations strings needed for API calls

apiKey and clientId must be defined in "apikey.properties" file in root directory
i.e.
API_KEY="XXXXXXXX"
CLIENT_ID="XXXXXXX"
 */
data class Credentials (
    var apiKey: String = BuildConfig.API_KEY,
    var clientId: String = BuildConfig.CLIENT_ID,
    var bearer: String = ""
)