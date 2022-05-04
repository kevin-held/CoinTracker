package com.example.cointracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    /*
    insert coin / update coin info
    used when adding detailed info from getCoin()
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplace(coinEntity: CoinEntity)

    /*
    insert coin no update
    used when initializing coin list from getAssetList()
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(coinEntity: CoinEntity)

    /*
    Used for getting coin list
     */
    @Query("Select * FROM coin_table ORDER BY name")
    fun getCoins(): LiveData<List<CoinEntity>>

    /*
    used for getting tracked coin list
     */
    @Query("Select * FROM coin_table WHERE tracked = 1 ORDER BY marketCapRank")
    fun getTrackedCoins(): LiveData<List<CoinEntity>>

    /*
    update coin to indicated that it should be tracked with detailed info
     */
    @Query("UPDATE coin_table SET tracked = 1 WHERE :name = name")
    suspend fun trackCoin(name: String)

    /*
    remove tracked coin from being tracked
     */
    @Query("UPDATE coin_table SET tracked = 0 WHERE :name = name")
    suspend fun untrackCoin(name: String)
}