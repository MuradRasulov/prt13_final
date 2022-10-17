package com.example.practika12_var4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
    private lateinit var city: EditText
    private lateinit var pressure: EditText
    private lateinit var wind_speed: EditText
    private lateinit var type_weather: EditText
    private lateinit var button: Button
    private lateinit var button2: Button

    private var index: Int = -1
    private lateinit var db: WeatherDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_weather)

        temperature = findViewById(R.id.editText_temperature)
        city = findViewById(R.id.editText_city)
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
            if (index == -1){
                addWeather(temperature.text.toString(), city.text.toString(), pressure.text.toString(), wind_speed.text.toString(), type_weather.text.toString())
            }
            else
            {
                updateWeather(temperature.text.toString(), city.text.toString(), pressure.text.toString(), wind_speed.text.toString(), type_weather.text.toString())
                super.onBackPressed()
            }
        }

        button2.setOnClickListener {
            val weather = weatherList[index]
            val weatherType = typeWeatherList[index]
            executor.execute{
                weatherDAO.killWeather(weather)
                weatherDAO.killTypeWeather(weatherType)
            }
            super.onBackPressed()
        }
    }

    private fun addWeather(temperature:String, city:String, pressure:String, wind_speed: String, type:String) {
        val uuidWeather = UUID.randomUUID()
        val uuidType = UUID.randomUUID()
        val weather = Weather(uuidWeather, temperature, city, pressure, wind_speed)
        val weatherType = WeatherType(uuidType, uuidWeather, type)
        Executors.newSingleThreadExecutor().execute {
            db.weatherDAO().addTypeWeather(weatherType)
            db.weatherDAO().addWeather(weather)
        }
    }

    private fun updateWeather(temperature:String, city:String, pressure:String, wind_speed: String, type:String){
        val uuidWeather = weatherList[index].idWeather
        val uuidType = typeWeatherList[index].weatherId
        val weather = Weather(uuidWeather, temperature, city, pressure, wind_speed)
        val weatherType = WeatherType(uuidType, uuidWeather, type)
        Executors.newSingleThreadExecutor().execute {
            db.weatherDAO().saveTypeWeather(weatherType)
            db.weatherDAO().saveWeather(weather)
        }
    }

    private fun updateListWeather(){
        weatherList.clear()
        typeWeatherList.clear()
        db.weatherDAO().getAllTypesWeather().observe(this, androidx.lifecycle.Observer {
            runOnUiThread(Runnable{
                kotlin.run {
                    typeWeatherList.addAll(it)
                    if(index > - 1) type_weather.setText(typeWeatherList[index].typeName)
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
            city.setText(weatherList[index].city)
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