package com.example.practika12_var4.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.practika12_var4.converters.DateConverter
import com.example.practika12_var4.models.Weather
import com.example.practika12_var4.models.WeatherType

@Database(entities = [Weather::class, WeatherType::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class WeatherDB: RoomDatabase(){
    abstract fun weatherDAO(): WeathersDAO
}