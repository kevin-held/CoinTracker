package com.example.cointracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

/*
Coin Entity representing all possible columns in coin database
 */
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
    var tracked: Boolean = false

    ) {
    /*
    Get formatted info
     */
    fun displayString(): String {
        var string =  "Price: $$price\n"
        string += "Market Cap Rank: $marketCapRank\n"
        string += "Market Cap: $${totalMarketCap.toLong()}\n"
        string += "Volume: $${volume.toLong()}\n"
        string += "Supply: $totalSupply\n"
        string += "Last Updated: ${getFormattedTimestamp()}"
        return string
    }

    /*
    get timestamp in readable format and local timezone
     */
    fun getFormattedTimestamp(): String{
        val incoming = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        incoming.setTimeZone(TimeZone.getTimeZone("UTC"))
        val date = incoming.parse(timestamp)
        val outgoing = SimpleDateFormat("yyyy-MM-dd hh:mm a")
        return outgoing.format(date.time)
    }

    /*
    check if timestamp is older than 10 minutes
     */
    fun needsUpdate(): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
        val date = sdf.parse(timestamp)

        val timestampInMillis = (date.time)
        val currentTimeInMillis = (Date().time)
        val tenMinutes = (60 * 10 * 1000)

        //Log.d("TIME STAMP", name + " " + timestampinmillis.toString() + " " + sdf.format(timestampinmillis))
        //Log.d("CURRENT TIME", currenttimeinmillis.toString() + " " + sdf.format(currenttimeinmillis))
        //Log.d("NEEDS UPDATE", "$needsupdate" )
        return currentTimeInMillis - timestampInMillis > tenMinutes
    }
}