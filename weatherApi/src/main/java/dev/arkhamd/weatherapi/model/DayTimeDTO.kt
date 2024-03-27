package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName

data class DayTime(
    val partOfDay: PartOfDay
)

enum class PartOfDay {
    @SerializedName("d")
    DAY,

    @SerializedName("n")
    NIGHT
}
