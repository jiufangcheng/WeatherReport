package com.example.sunnyweather.logic.enity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Place",
        indices = [Index(value = ["name"], unique = true)])
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val name:String,
    val location:String,

)