package com.example.practika12_var4.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = WEATHER_TYPE_TABLE)
data class WeatherType(
    @PrimaryKey(autoGenerate = false)
    val id: UUID,
    @ColumnInfo(index =  true)
    var weatherId: UUID,
    var typeName: String
)