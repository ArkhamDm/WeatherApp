package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo

data class SnowInfoDBO(
    @ColumnInfo("3h") val threeHours: Float
)
