package com.example.cointracker.model

/*
Class representing JSON response for getAssetList()
Assets represent individual items in the AssetList
only id, name, and symbol are returned by the api when getting the AssetList
for detailed info see Coin object
 */
data class Asset(
    val id: String,
    val name: String,
    val symbol: String,

    // below is other possible fields that are not used by this app
    //val slugName: String,
    //val status: String,
    //val type: String,
    //val url: String,
    //val contractAddress: String
)
