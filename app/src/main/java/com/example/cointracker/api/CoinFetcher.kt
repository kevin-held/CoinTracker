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

    fun getToken(): LiveData<String> {
        var responseLiveData: MutableLiveData<String> = MutableLiveData()
        val request: Call<Token> = braveNewCoinApi.getToken(key = credentials.apiKey, requestBody = RequestBody())
        //val response = request.execute()
        //val token: Token? = response.body()
        //credentials.bearer = "Bearer " + token?.access_token
        request.enqueue(object: Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                Log.d(TAG, "Response Received: ${response.body().toString()}")
                var token: Token? = response.body()
                //credentials.bearer = "Bearer " + token?.access_token
                responseLiveData.value = "Bearer " + token?.access_token
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.d(TAG, "Response Failed: ${t.message}")
            }
        })
        return responseLiveData
    }

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

    fun getCoin(asset: Asset, bearer: String, func: (input: CoinEntity) -> Unit){
        val request: Call<CoinList> = braveNewCoinApi.getCoin(key = credentials.apiKey, authorization = bearer, assetId = asset.id)
        request.enqueue(object: Callback<CoinList> {
            override fun onResponse(call: Call<CoinList>, response: Response<CoinList>) {
                Log.d(TAG, "Response Received: ${response.body().toString()}")
                var coin: Coin? = response.body()?.content?.get(0)
                if (coin != null){
                    var coinEntity: CoinEntity = CoinEntity(
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