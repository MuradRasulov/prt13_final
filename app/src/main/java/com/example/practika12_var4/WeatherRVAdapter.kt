package com.example.practika12_var4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practika12_var4.models.Weather
import com.example.practika12_var4.models.WeatherType

class WeatherRVAdapter(context: Context?, val data: MutableList<Weather>, val dataType : MutableList<WeatherType>): RecyclerView.Adapter<WeatherRVAdapter.WeatherViewHolder?>() {

    private val layoutInflater: LayoutInflater = android.view.LayoutInflater.from(context)
    private var iClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view: View = layoutInflater.inflate(R.layout.item_weather_layout, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = data[position]
        val itemType = dataType[position]
        holder.temperatureTextView.text = item.temperature
        holder.cityTextView.text = item.city
        holder.pressureTextView.text = item.pressure
        holder.speedTextView.text = item.wind_speed
        holder.typeTextView.text = itemType.typeName
    }

    override fun getItemCount(): Int = data.size

    inner class WeatherViewHolder(item: View): RecyclerView.ViewHolder(item), View.OnClickListener {
        var temperatureTextView: TextView = item.findViewById(R.id.tv_temperature)
        var cityTextView: TextView = item.findViewById(R.id.tv_city)
        var pressureTextView: TextView = item.findViewById(R.id.tv_pressure)
        var speedTextView: TextView = item.findViewById(R.id.tv_speed)
        var typeTextView : TextView = item.findViewById(R.id.tv_type)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            iClickListener?.onItemClick(view, adapterPosition)
        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?){
        iClickListener = itemClickListener
    }

    interface ItemClickListener{
        fun onItemClick(view: View?, position: Int)
    }
}