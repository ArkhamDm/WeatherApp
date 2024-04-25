package dev.arkhamd.weaherdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.arkhamd.weaherdatabase.model.CityInfoDBO
import dev.arkhamd.weaherdatabase.model.DayWeatherInfoDBO
import dev.arkhamd.weaherdatabase.model.HourWeatherInfoDBO
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface WeatherInfoDao {

    @Query("SELECT * from city_info")
    fun getAllCityInfo(): Single<CityInfoDBO>
    @Query("SELECT * from hour_weather_info")
    fun getAllHourInfo(): Single<List<HourWeatherInfoDBO>>
    @Query("SELECT * from day_weather_info")
    fun getAllDayInfo(): Single<List<DayWeatherInfoDBO>>

    @Insert
    fun insertCityInfo(cityInfoDBO: CityInfoDBO): Completable
    @Insert
    fun insertHourInfo(hourWeatherInfoDBO: List<HourWeatherInfoDBO>): Completable
    @Insert
    fun insertDayInfo(dayWeatherInfoDBO: List<DayWeatherInfoDBO>): Completable

    @Query("DELETE FROM city_info")
    fun clearCityInfo(): Completable

    @Query("DELETE FROM hour_weather_info")
    fun clearHourWeatherInfo(): Completable

    @Query("DELETE FROM day_weather_info")
    fun clearDayWeatherInfo(): Completable

    fun clearAllInfo(): Completable {
        return clearCityInfo()
            .andThen(clearDayWeatherInfo())
            .andThen(clearHourWeatherInfo())
    }
}