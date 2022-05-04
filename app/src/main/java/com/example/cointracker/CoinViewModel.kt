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

    var token: LiveData<String> // bearer token required for getCoin()
    var assetListLiveData: LiveData<AssetList> // list of all coins only asset data (assetId, name, symbol)
    var coinListLiveData: LiveData<List<CoinEntity>> // list of all coins Tracked or Untracked (can have detailed info)
    var trackedListLiveData: LiveData<List<CoinEntity>> // list of all Tracked coins (must have detailed info)

    private val coinFetcher: CoinFetcher = CoinFetcher() // for making api calls

    init {
        token = coinFetcher.getToken()
        assetListLiveData = coinFetcher.getAssetList{ loadAssets() }
        coinListLiveData = dao.getCoins()
        trackedListLiveData = dao.getTrackedCoins()
    }

    /*
    add coin to tracked list
    get detailed info
     */
    fun trackCoin(asset: Asset, name: String){
        viewModelScope.launch {
            dao.trackCoin(name)
            getCoin(asset)
        }

    }

    /*
    remove from tracked list
     */
    fun untrackCoin(name: String){
        viewModelScope.launch {
            dao.untrackCoin(name)
        }
    }

    /*
    get detailed info for a coin
     */
    private fun getCoin(asset: Asset) {
        if (token.value != null && token.value != ""){
            coinFetcher.getCoin(asset, token.value!!) {
                loadCoin(it)
            }
        }
    }


    /*
    add a coin to the database
     */
    private fun loadCoin(coinEntity: CoinEntity){
        viewModelScope.launch {
            dao.insertReplace(coinEntity)
        }
    }


    /*
    add all assets to the database
     */
    private fun loadAssets(){
        viewModelScope.launch {
            assetListLiveData.value?.content?.forEach {
                val coinEntity = CoinEntity(assetId = it.id, name = it.name, symbol = it.symbol)
                dao.insertIgnore(coinEntity)
            }
        }
    }
}