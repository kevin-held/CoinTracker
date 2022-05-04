package com.example.cointracker.api

import com.example.cointracker.model.*
import retrofit2.Call
import retrofit2.http.*

interface BraveNewCoinApi {

    /*
    Get List of Coins with info such as ASSET ID, NAME, SYMBOL
     */
    @Headers(
        "X-RapidAPI-Host: bravenewcoin.p.rapidapi.com",
    )
    @GET("asset?status=ACTIVE&type=CRYPTO")
    fun getAssetList(@Header("X-RapidAPI-Key") key: String): Call<AssetList>


    /*
    Post clientID to get Bearer token for authorization string used in getCoin
     */
    @Headers(
        "content-type: application/json",
        "X-RapidAPI-Host: bravenewcoin.p.rapidapi.com"
    )
    @POST("/oauth/token")
    fun getToken(@Header("X-RapidAPI-Key") key: String,
                 @Body requestBody: RequestBody): Call<Token>


    /*
    Get detailed information on a single coin
     */
    @Headers(
        "X-RapidAPI-Host: bravenewcoin.p.rapidapi.com",
    )
    @GET("market-cap")
    fun getCoin(@Header("X-RapidAPI-Key") key: String,
                @Header("Authorization") authorization: String,
                @Query("assetId") assetId: String): Call<CoinList>

}