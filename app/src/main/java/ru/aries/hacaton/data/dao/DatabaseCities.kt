package ru.aries.hacaton.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.aries.hacaton.models.api.CityDao
import ru.aries.hacaton.models.api.CountryDao
import ru.aries.hacaton.models.api.RegionDao

@Database(
    entities = [
        CityDao::class,
        RegionDao::class,
        CountryDao::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseCities : RoomDatabase() {

    abstract val cityDb: CityDaoRoom

    companion object Factory {
        fun build(appContext: Context): DatabaseCities =
            Room.databaseBuilder(
                appContext,
                DatabaseCities::class.java,
                "database_cities_v1"
            ).fallbackToDestructiveMigration()
                .build()
    }
}