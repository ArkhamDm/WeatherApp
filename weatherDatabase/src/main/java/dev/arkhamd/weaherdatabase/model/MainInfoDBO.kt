package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MainInfoDBO(
    @ColumnInfo("temp") val temp: Int,
    @ColumnInfo("feels_like") val feelsLike: Int,
    @ColumnInfo("temp_min") val tempMin: Int,
    @ColumnInfo("temp_max") val tempMax: Int,
    @ColumnInfo("pressure") val pressure: Int,
    @ColumnInfo("humidity") val humidity: Int,
)
