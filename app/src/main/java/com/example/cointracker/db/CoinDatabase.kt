package com.example.cointracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
Coin Database Singleton Object
 */
@Database(entities = [CoinEntity::class], version = 1, exportSchema = false)
abstract class CoinDatabase: RoomDatabase() {
    abstract val coinDao: CoinDao

    companion object {
        @Volatile
        private var INSTANCE: CoinDatabase? = null

        fun getInstance(context: Context): CoinDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CoinDatabase::class.java,
                        "coin_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}