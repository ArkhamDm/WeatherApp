package dev.arkhamd.weaherdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.arkhamd.weaherdatabase.dao.WeatherInfoDao
import dev.arkhamd.weaherdatabase.model.WeatherInfoDBO

class WeatherDatabase internal constructor(private val database: WeatherRoomDatabase) {

    val weatherInfoDao: WeatherInfoDao
        get() = database.weatherInfoDao()

}

@Database(entities = [WeatherInfoDBO::class], version = 1, exportSchema = false)
abstract class WeatherRoomDatabase: RoomDatabase() {

    abstract fun weatherInfoDao(): WeatherInfoDao

}

fun WeatherDatabase(applicationContext: Context): WeatherDatabase {
    val weatherRoomDatabase =  Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        WeatherRoomDatabase::class.java,
        "weather"
    ).build()
    return WeatherDatabase(weatherRoomDatabase)
}