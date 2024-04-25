package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_info")
data class CityInfoDBO(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("city") val name: String,
    @ColumnInfo("sunrise") val sunriseTime: Long,
    @ColumnInfo("sunset") val sunsetTime: Long
)
