package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherInfo")
data class WeatherInfoDBO(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("dt") val time: Long,
    @Embedded(prefix = "main") val mainInfoDBO: MainInfoDBO,
    @Embedded(prefix = "weather") val weatherDescriptionDBO: WeatherDescriptionDBO,
    @Embedded(prefix = "clouds") val cloudInfoDBO: CloudInfoDBO,
    @Embedded(prefix = "wind") val windInfoDBO: WindInfoDBO,
    @ColumnInfo("visibility") val visibility: Int,
    @ColumnInfo("pop") val probOfPrecipitation: Float,
    @Embedded(prefix = "rain") val rainInfoDBO: RainInfoDBO? = null,
    @Embedded(prefix = "snow") val snowInfoDBO: SnowInfoDBO? = null,
    @ColumnInfo("dt_txt") val timeTxt: String,
)
enum class PartOfDay {
    DAY,
    NIGHT
}