package com.example.cointracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplace(coinEntity: CoinEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(coinEntity: CoinEntity)

    @Query("Select * FROM coin_table ORDER BY marketCapRank")
    fun getCoins(): LiveData<List<CoinEntity>>

    @Query("Select * FROM coin_table WHERE tracked = 1 ORDER BY marketCapRank")
    fun getTrackedCoins(): LiveData<List<CoinEntity>>

    @Query("UPDATE coin_table SET tracked = 1 WHERE :name = name")
    suspend fun trackCoin(name: String)

    @Query("UPDATE coin_table SET tracked = 0 WHERE :name = name")
    suspend fun untrackCoin(name: String)
}