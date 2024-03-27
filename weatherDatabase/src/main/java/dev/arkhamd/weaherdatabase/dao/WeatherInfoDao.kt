package dev.arkhamd.weaherdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.arkhamd.weaherdatabase.model.WeatherInfoDBO
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface WeatherInfoDao {

    @Query("SELECT * FROM weatherInfo")
    fun getAll(): Single<List<WeatherInfoDBO>>

    @Insert
    fun insert(weatherInfo: List<WeatherInfoDBO>): Completable

    @Delete
    fun delete(weatherInfoDBO: List<WeatherInfoDBO>): Completable

    @Query("DELETE FROM weatherInfo")
    fun clear(): Completable
}