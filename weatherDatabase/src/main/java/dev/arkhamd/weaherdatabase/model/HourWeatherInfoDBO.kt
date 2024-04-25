package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hour_weather_info")
data class HourWeatherInfoDBO(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("dt") val time: Long,
    @ColumnInfo("temp") val temp: String,
    @ColumnInfo("feels_like") val feelsLike: String,
    @ColumnInfo("humidity") val humidity: String,
    @ColumnInfo("wind_speed") val windSpeed: String,
    @ColumnInfo("weather_condition") val weatherCondition: String,
    @ColumnInfo("weather_condition_icon_id") val weatherConditionIconId: Int,
    @ColumnInfo("dt_txt") val timeTxt: String,
)