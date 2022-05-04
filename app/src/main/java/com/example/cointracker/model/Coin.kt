package com.example.cointracker.model

/*
Class representing JSON response for getCoin()
 */
data class Coin (
    val id: String,
    val assetId: String,
    val timestamp: String,
    val marketCapRank: Int,
    val volumeRank: Int,
    val price: Double,
    val volume: Double,
    val totalSupply: Long,
    val freeFloatSupply: Long,
    val marketCap: Double,
    val totalMarketCap: Double
        )