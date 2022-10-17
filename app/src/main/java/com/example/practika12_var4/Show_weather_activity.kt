package com.example.practika12_var4

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.practika12_var4.data.WeatherDB
import com.example.practika12_var4.models.DATABASE_NAME
import com.example.practika12_var4.models.Weather
import com.example.practika12_var4.models.WeatherType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Show_weather_activity : AppCompatActivity() {
    private var weatherList: MutableList<Weather> = mutableListOf()
    private var typeWeatherList: MutableList<WeatherType> = mutableListOf()

    private lateinit var rv: RecyclerView
    private var indexChanged = -1
    private lateinit var adapter: WeatherRVAdapter
    private lateinit var db: WeatherDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_weather_activity)

        db = Room.databaseBuilder(this, WeatherDB::class.java, DATABASE_NAME).build()
    }

    private fun getAllWeather(){
        typeWeatherList.clear()
        weatherList.clear()
        db.weatherDAO().getAllTypesWeather().observe(this, androidx.lifecycle.Observer {
            typeWeatherList.addAll(it)
        })
        db.weatherDAO().getAllWeathers().observe(this, androidx.lifecycle.Observer {
            weatherList.addAll(it)
            updateRecycle()
        })
    }

    private fun updateRecycle(){
        adapter = WeatherRVAdapter(this, weatherList, typeWeatherList)
        val rvListener = object : WeatherRVAdapter.ItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@Show_weather_activity, Add_weather_activity::class.java)
                intent.putExtra("num", position)
                indexChanged = position
                startActivity(intent)
                Toast.makeText(this@Show_weather_activity, "Вы выбрали $position вкладку", Toast.LENGTH_SHORT).show()
            }
        }
        adapter.setClickListener(rvListener)
        rv = findViewById(R.id.recyclerView)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
    }

    fun back(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        getAllWeather()
    }
}