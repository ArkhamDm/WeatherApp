package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo

data class WeatherDescriptionDBO(
    @ColumnInfo("cond") val condId: Int,
    @ColumnInfo("main") val condMain: String,
    @ColumnInfo("description") val description: String,
)
