package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo

data class RainInfoDBO(
     @ColumnInfo("3h") val threeHours: Float
)