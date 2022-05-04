package com.example.cointracker.api

import com.example.cointracker.model.*
import retrofit2.Call
import retrofit2.http.*

interface BraveNewCoinApi {

    @Headers(
        "X-RapidAPI-Host: bravenewcoin.p.rapidapi.com",
    )
    @GET("asset?status=ACTIVE&type=CRYPTO")
    fun getAssetList(@Header("X-RapidAPI-Key") key: String): Call<AssetList>


    @Headers(
        "content-type: application/json",
        "X-RapidAPI-Host: bravenewcoin.p.rapidapi.com"
    )
    @POST("/oauth/token")
    fun getToken(@Header("X-RapidAPI-Key") key: String,
                 @Body requestBody: RequestBody): Call<Token>


    @Headers(
        "X-RapidAPI-Host: bravenewcoin.p.rapidapi.com",
    )
    @GET("market-cap")
    fun getCoin(@Header("X-RapidAPI-Key") key: String,
                @Header("Authorization") authorization: String,
                @Query("assetId") assetId: String): Call<CoinList>



}