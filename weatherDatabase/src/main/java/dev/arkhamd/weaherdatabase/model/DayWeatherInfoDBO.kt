package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_weather_info")
data class DayWeatherInfoDBO(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("temp") val temp: String,
    @ColumnInfo("icon_id") val iconId: Int,
    @ColumnInfo("cond_main") val condMain: String,
    @ColumnInfo("humidity") val humidity: String,
    @ColumnInfo("wind_speed") val windSpeed: String,
    @ColumnInfo("time") val time: Long,
    @ColumnInfo("time_txt") val timeTxt: String,
)
