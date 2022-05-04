package com.example.cointracker.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cointracker.db.CoinEntity
import com.example.cointracker.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val TAG = "Coin Fetcher"

/*
Class containing retrofit calls to get data from API
 */
class CoinFetcher {

    private val braveNewCoinApi: BraveNewCoinApi
    private val credentials: Credentials

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://bravenewcoin.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        braveNewCoinApi = retrofit.create(BraveNewCoinApi::class.java)
        credentials = Credentials()
    }

    /*
    Get authorization token required for getCoin()
     */
    fun getToken(): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val request: Call<Token> = braveNewCoinApi.getToken(key = credentials.apiKey, requestBody = RequestBody(client_id = credentials.clientId))
        request.enqueue(object: Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                Log.d(TAG, "Response Received: ${response.body().toString()}")
                val token: Token? = response.body()
                responseLiveData.value = "Bearer " + token?.access_token
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.d(TAG, "Response Failed: ${t.message}")
            }
        })
        return responseLiveData
    }

    /*
    Get Asset List, containing <assetID>, <name>, <symbol> for all available coins
     */
    fun getAssetList(func: () -> Unit): LiveData<AssetList> {
        val responseLiveData: MutableLiveData<AssetList> = MutableLiveData()
        val request: Call<AssetList> = braveNewCoinApi.getAssetList(key = credentials.apiKey)
        request.enqueue(object: Callback<AssetList> {
            override fun onResponse(call: Call<AssetList>, response: Response<AssetList>) {
                Log.d(TAG, "Response Received: ${response.body().toString()}")
                responseLiveData.value = response.body()
                func()
            }

            override fun onFailure(call: Call<AssetList>, t: Throwable) {
                Log.d(TAG, "Response Failed: ${t.message}")
            }
        })
        return responseLiveData
    }

    /*
    Get detailed info on a specific coin
    requires Bearer Token from getToken()
     */
    fun getCoin(asset: Asset, bearer: String, func: (input: CoinEntity) -> Unit){
        val request: Call<CoinList> = braveNewCoinApi.getCoin(key = credentials.apiKey, authorization = bearer, assetId = asset.id)
        request.enqueue(object: Callback<CoinList> {
            override fun onResponse(call: Call<CoinList>, response: Response<CoinList>) {
                Log.d(TAG, "Response Received: ${response.body().toString()}")
                val coin: Coin? = response.body()?.content?.get(0)
                if (coin != null){
                    val coinEntity = CoinEntity(
                        assetId = coin.assetId,
                        name = asset.name,
                        timestamp = coin.timestamp,
                        symbol = asset.symbol,
                        marketCapRank = coin.marketCapRank,
                        price = coin.price,
                        totalMarketCap = coin.marketCap,
                        volume = coin.volume,
                        totalSupply = coin.totalSupply,
                        tracked = true)
                    func(coinEntity)
                }

            }

            override fun onFailure(call: Call<CoinList>, t: Throwable) {
                Log.d(TAG, "Response Failed: ${t.message}")
            }
        })
    }
}