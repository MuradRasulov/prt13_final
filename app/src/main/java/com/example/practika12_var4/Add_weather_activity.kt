package com.example.practika12_var4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import com.example.practika12_var4.data.WeatherDB
import com.example.practika12_var4.models.*
import com.example.practika12_var4.models.DATABASE_NAME
import java.util.*
import java.util.concurrent.Executors

class Add_weather_activity : AppCompatActivity() {
    private var weatherList: MutableList<Weather> = mutableListOf()
    private var typeWeatherList: MutableList<WeatherType> = mutableListOf()

    private lateinit var temperature: EditText
    private lateinit var town: EditText
    private lateinit var pressure: EditText
    private lateinit var wind_speed: EditText
    private lateinit var type_weather: EditText
    private lateinit var button: Button
    private lateinit var button2: Button

    private var index: Int = -1
    private lateinit var db: WeatherDB

    //private var check: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_weather)

        temperature = findViewById(R.id.editText_temperature)
        town = findViewById(R.id.editText_city)
        pressure = findViewById(R.id.editText_pressure)
        wind_speed = findViewById(R.id.editText_wind_speed)
        type_weather = findViewById(R.id.editText_typeWeather)

        button = findViewById(R.id.add_weather)
        button2 = findViewById(R.id.Delete_weather)

        db = Room.databaseBuilder(this, WeatherDB::class.java, DATABASE_NAME).build()

        val weatherDAO = db.weatherDAO()
        val executor = Executors.newSingleThreadExecutor()

        index = intent.getIntExtra("num", -1)

        updateListWeather()

        if(index == -1) button2.visibility = View.INVISIBLE

        button.setOnClickListener {

            db.weatherDAO().getAllTypesWeather().observe(this, androidx.lifecycle.Observer {
                typeWeatherList.addAll(it)
            })

            if (index == -1){
                addWeather(temperature.text.toString(), town.text.toString(), pressure.text.toString(), wind_speed.text.toString(), type_weather.text.toString())
            }
            else
            {
                updateWeather(temperature.text.toString(), town.text.toString(), pressure.text.toString(), wind_speed.text.toString(), type_weather.text.toString())
            }
        }

        button2.setOnClickListener {
            val weather = weatherList[index]
            executor.execute{
                weatherDAO.killWeather(weather)
            }
            super.onBackPressed()
        }
    }

    private fun addWeather(temperature:String, city:String, pressure:String, wind_speed: String, type:String) {
        var WeatherContains: Boolean = true
        var WeatherTypeContains: Boolean = true
        val uuidWeather = UUID.randomUUID()
        val uuidType = UUID.randomUUID()
        typeWeatherList.forEach(){
            if (it.typeName == type_weather.text.toString()){
                WeatherContains = false
            }
        }
        weatherList.forEach(){
            if (it.city == town.text.toString()){
                WeatherTypeContains = false
            }
        }
        if (WeatherContains && WeatherTypeContains){
            val weatherType = WeatherType(uuidType, type)
            val weather = Weather(uuidWeather, temperature, city, pressure, wind_speed, uuidType)
            Executors.newSingleThreadExecutor().execute {
                db.weatherDAO().addTypeWeather(weatherType)
                db.weatherDAO().addWeather(weather)
            }
        }
        else if (!WeatherContains && WeatherTypeContains){
            Executors.newSingleThreadExecutor().execute {
                val uuid = db.weatherDAO().getType(type)
                val weather = Weather(uuidWeather, temperature, city, pressure, wind_speed, uuid)
                db.weatherDAO().addWeather(weather)
            }
        }
        else
        {
            Toast.makeText(this, "Уже имеется", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWeather(temperature:String, city:String, pressure:String, wind_speed: String, type:String){
        var WeatherContains: Boolean = true
        var WeatherTypeContains: Boolean = true
        val uuidWeather = weatherList[index].idWeather
        val uuidType = weatherList[index].typeId

        typeWeatherList.forEach(){
            Log.d("t", it.typeName)
        }
        weatherList.forEach(){
            Log.d("t", it.city)
        }
        typeWeatherList.forEach(){
            if (it.typeName == type_weather.text.toString()){
                WeatherContains = false
            }
        }
        weatherList.forEach(){
            if (it.city == town.text.toString()){
                WeatherTypeContains = false
            }
        }
        if (WeatherContains && WeatherTypeContains){
            val weatherType = WeatherType(uuidType, type)
            val weather = Weather(uuidWeather, temperature, city, pressure, wind_speed, uuidType)
            Executors.newSingleThreadExecutor().execute {
                db.weatherDAO().saveTypeWeather(weatherType)
                db.weatherDAO().saveWeather(weather)
            }
        }
        else if (!WeatherContains && WeatherTypeContains){
            Executors.newSingleThreadExecutor().execute {
                val weather = Weather(uuidWeather, temperature, city, pressure, wind_speed, uuidType)
                db.weatherDAO().saveWeather(weather)
            }
        }
        else
        {
            super.onBackPressed()
        }
    }

    private fun updateListWeather(){
        weatherList.clear()
        typeWeatherList.clear()
        db.weatherDAO().getAllTypesWeather().observe(this, androidx.lifecycle.Observer {
            runOnUiThread(Runnable{
                kotlin.run {
                    typeWeatherList.addAll(it)
                }
            })
        })
        db.weatherDAO().getAllWeathers().observe(this, androidx.lifecycle.Observer {
            runOnUiThread(Runnable{
                kotlin.run {
                    weatherList.addAll(it)
                    selectWeather(index)
                }
            })
        })
    }

    private fun selectWeather(index: Int){
        if (index > -1){
            temperature.setText(weatherList[index].temperature)
            town.setText(weatherList[index].city)
            pressure.setText(weatherList[index].pressure)
            wind_speed.setText(weatherList[index].wind_speed.toString())
            button.setText(R.string.update_weather)
            button2.visibility = View.VISIBLE
        }
    }

    fun back(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }
}