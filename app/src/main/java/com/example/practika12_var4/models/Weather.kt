package com.example.practika12_var4.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = WEATHER_TABLE)
data class Weather(
    @PrimaryKey(autoGenerate = false)
    val idWeather: UUID,
    var temperature: String,
    var city: String,
    var pressure: String,
    var wind_speed: String,
    var typeId: UUID
)