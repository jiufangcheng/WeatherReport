package com.example.sunnyweather.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.dao.PlaceManagerDao
import com.example.sunnyweather.logic.enity.Place

@Database(
    entities = [Place::class],
    version = 1,
    exportSchema = true  // 启用导出
)
abstract class PlaceDatabase :RoomDatabase(){
    abstract fun placeManagerDao():PlaceManagerDao
    companion object {

        private var instance: PlaceDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): PlaceDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                PlaceDatabase::class.java, "app_database")
                .build().apply {
                    instance = this
                }
        }
    }
}