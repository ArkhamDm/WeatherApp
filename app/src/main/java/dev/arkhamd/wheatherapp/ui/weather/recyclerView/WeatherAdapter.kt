package dev.arkhamd.wheatherapp.ui.weather.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.arkhamd.data.model.HourWeatherInfo
import dev.arkhamd.wheatherapp.R

class WeatherAdapter(
    private val hourWeatherInfoList: List<HourWeatherInfo>,
    private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item_radio_button, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        hourWeatherInfoList[position].apply {
            holder.weatherImage.setImageResource(weatherConditionIconId)
            holder.timeText.text = timeTxt.substring(11, 16)
            holder.descText.text = weatherCondition
        }

        if (position == selectedItemPosition) {
            holder.bgImage.setImageResource(R.drawable.cirle_weather_item_selected)
        } else {
            holder.bgImage.setImageResource(R.drawable.cirle_weather_item)
        }


        holder.itemView.setOnClickListener {
            val previousItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition

            notifyItemChanged(previousItemPosition)
            notifyItemChanged(selectedItemPosition)

            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return hourWeatherInfoList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bgImage: ImageView
        val weatherImage: ImageView
        val timeText: TextView
        val descText: TextView

        init {
            itemView.apply {
                bgImage = findViewById(R.id.circleShapeImage)
                weatherImage = findViewById(R.id.hourWeatherIcon)
                timeText = findViewById(R.id.timeText)
                descText = findViewById(R.id.discriptionText)
            }
        }
    }
}