package dev.arkhamd.weaherdatabase.model

import androidx.room.ColumnInfo

data class CloudInfoDBO(
    @ColumnInfo("all") val cloudiness: Int
)
