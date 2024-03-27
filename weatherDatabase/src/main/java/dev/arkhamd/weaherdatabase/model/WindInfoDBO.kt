package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo

data class WindInfoDBO(
    @ColumnInfo("speed") val speed: Int,
    @ColumnInfo("deg") val directionInDegrees: Int,
)
