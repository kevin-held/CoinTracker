package com.example.cointracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="coin_table")
data class CoinEntity(
    @PrimaryKey
    var assetId: String = "",
    @ColumnInfo(name="name")
    var name: String = "",
    @ColumnInfo(name="symbol")
    var symbol: String = "",
    @ColumnInfo(name="timestamp")
    var timestamp: String = "",
    @ColumnInfo(name="marketCapRank")
    var marketCapRank: Int = 0,
    @ColumnInfo(name="price")
    var price: Double = 0.0,
    @ColumnInfo(name="volume")
    var volume: Double = 0.0,
    @ColumnInfo(name="totalSupply")
    var totalSupply: Long = 0L,
    @ColumnInfo(name="totalMarketCap")
    var totalMarketCap: Double = 0.0,
    @ColumnInfo(name="tracked")
    var tracked: Boolean = false,

    ) {
    fun displayString(): String {
        var string =  "Price: $$price\n"
        string += "Market Cap Rank: $marketCapRank\n"
        string += "Market Cap: $${totalMarketCap.toInt()}\n"
        string += "Volume: $${volume.toInt()}\n"
        string += "Supply: $totalSupply\n"
        string += "Last Updated: $timestamp"
        return string
    }
}