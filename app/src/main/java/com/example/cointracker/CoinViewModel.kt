package com.example.cointracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cointracker.api.CoinFetcher
import com.example.cointracker.db.CoinDao
import com.example.cointracker.db.CoinEntity
import com.example.cointracker.model.Asset
import com.example.cointracker.model.AssetList
import kotlinx.coroutines.launch

class CoinViewModel(val dao: CoinDao): ViewModel() {

    var token: LiveData<String>
    var assetListLiveData: LiveData<AssetList>
    var coinListLiveData: LiveData<List<CoinEntity>>
    var trackedListLiveData: LiveData<List<CoinEntity>>

    private val coinFetcher: CoinFetcher = CoinFetcher()
    //private var initCoins = false

    init {
        token = coinFetcher.getToken()
        assetListLiveData = coinFetcher.getAssetList{ loadAssets() }
        coinListLiveData = dao.getCoins()
        trackedListLiveData = dao.getTrackedCoins()
    }

    fun trackCoin(asset: Asset, name: String){
        viewModelScope.launch {
            dao.trackCoin(name)
            getCoin(asset)
        }

    }

    fun untrackCoin(name: String){
        viewModelScope.launch {
            dao.untrackCoin(name)
        }
    }

    fun getToken(){
        token = coinFetcher.getToken()
    }

    fun getAssets(){
        assetListLiveData = coinFetcher.getAssetList{ loadAssets() }
    }

    /*
    fun getCoins(test: String){
        Log.d("CoinViewModel", "get Coins called ${token.value}")
        if (token.value != null && token.value != "" && initCoins != true) {
            initCoins = true
            assetListLiveData.value?.content?.forEach{
                coinFetcher.getCoin(it.id, token.value!!) {
                    loadCoin(it)
                }
            }
        }
    }*/

    fun getCoin(asset: Asset) {
        if (token.value != null && token.value != ""){
            coinFetcher.getCoin(asset, token.value!!) {
                loadCoin(it)
            }
        }
    }


    private fun loadCoin(coinEntity: CoinEntity){
        viewModelScope.launch {
            dao.insertReplace(coinEntity)
        }
    }


    private fun loadAssets(){
        viewModelScope.launch {
            assetListLiveData.value?.content?.forEach {
                val coinEntity = CoinEntity(assetId = it.id, name = it.name, symbol = it.symbol)
                dao.insertIgnore(coinEntity)
            }
        }
    }
}