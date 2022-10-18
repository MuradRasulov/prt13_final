package com.example.practika12_var4.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.practika12_var4.models.*
import com.example.practika12_var4.models.WEATHER_TABLE
import com.example.practika12_var4.models.WEATHER_TYPE_TABLE
import java.util.UUID

@Dao
interface WeathersDAO {
    /* Таблица Погоды*/
    @Query("SELECT * FROM $WEATHER_TABLE")
    fun getAllWeathers(): LiveData<MutableList<Weather>>

    @Insert
    fun addWeather(weather: Weather)

    @Update
    fun saveWeather(weather: Weather)

    @Delete
    fun killWeather(weather: Weather)

    /* Таблица Подробностей*/
    @Query("SELECT * FROM $WEATHER_TYPE_TABLE")
    fun getAllTypesWeather(): LiveData<MutableList<WeatherType>>

    @Query("SELECT id FROM $WEATHER_TYPE_TABLE WHERE typeName = :name")
    fun getType(name:String):UUID

    @Insert
    fun addTypeWeather(weatherType: WeatherType)

    @Update
    fun saveTypeWeather(weatherType: WeatherType)

    @Delete
    fun killTypeWeather(weatherType: WeatherType)
}