package com.example.sunnyweather.logic.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sunnyweather.logic.enity.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceManagerDao{

    @Insert
    suspend fun insert(place:Place): Long

    @Query("select * from Place")
    fun getAllPlaces(): LiveData<List<Place>>
    @Delete
    suspend fun deletePlace(place: Place): Int
    @Query("select * from Place where name like '%' || :name || '%'")
    suspend fun queryPlaces(name:String):List<Place>
    @Query("select * from Place where name = :name")
    suspend fun queryPlace(name: String): Place
}