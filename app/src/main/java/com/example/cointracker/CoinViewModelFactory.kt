package com.example.cointracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cointracker.db.CoinDao

/*
CoinViewModelFactory
used to create a ViewModel which requires a DAO
 */
class CoinViewModelFactory (private val dao: CoinDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinViewModel::class.java)){
            return CoinViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}