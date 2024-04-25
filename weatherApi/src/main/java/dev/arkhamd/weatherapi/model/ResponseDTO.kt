package dev.arkhamd.weatherapi.model

import com.google.gson.annotations.SerializedName


/**
{
    "cod": "200",
    "message": 0,
    "cnt": 40,
    "list": [
    {
        "dt": 1661871600,
        "main": {
        "temp": 296.76,
        "feels_like": 296.98,
        "temp_min": 296.76,
        "temp_max": 297.87,
        "pressure": 1015,
        "sea_level": 1015,
        "grnd_level": 933,
        "humidity": 69,
        "temp_kf": -1.11
        },
        "weather": [
        {
            "id": 500,
            "main": "Rain",
            "description": "light rain",
            "icon": "10d"
        }
        ],
        "clouds": {
        "all": 100
        },
        "wind": {
        "speed": 0.62,
        "deg": 349,
        "gust": 1.18
        },
        "visibility": 10000,
        "pop": 0.32,
        "rain": {
        "3h": 0.26
        },
        "sys": {
        "pod": "d"
        },
        "dt_txt": "2022-08-30 15:00:00"
    },
    {
    ....
    }
    ],
    "city": {
    "id": 3163858,
    "name": "Zocca",
    "coord": {
    "lat": 44.34,
    "lon": 10.99
    },
    "country": "IT",
    "population": 4593,
    "timezone": 7200,
    "sunrise": 1661834187,
    "sunset": 1661882248
}
}
*/
data class ResponseDTO(
    @SerializedName("list") val weatherInfoDTO: List<WeatherInfoDTO>,
    @SerializedName("city") val cityInfoDTO: CityInfoDTO,
)