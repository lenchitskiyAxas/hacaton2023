package ru.aries.hacaton.models.api

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class ListCity(
    val data: List<City>
)

@Entity(tableName = CityDao.TABLE_NAME)
data class CityDao(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = REGION_ID) val regionId: Int,
    @ColumnInfo(name = COUNTRY_ID) val countryId: Int,
) {
    companion object {
        const val TABLE_NAME = "CityDao"
        const val ID = "id"
        const val NAME = "name"
        const val REGION_ID = "region_id"
        const val COUNTRY_ID = "country_id"
    }
}

@Entity(tableName = RegionDao.TABLE_NAME)
data class RegionDao(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = COUNTRY_ID) val countryId: Int,
) {
    companion object {
        const val TABLE_NAME = "RegionDao"
        const val ID = "id"
        const val NAME = "name"
        const val COUNTRY_ID = "country_id"
    }
}

@Entity(tableName = CountryDao.TABLE_NAME)
data class CountryDao(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = NAME) val name: String
) {
    companion object {
        const val TABLE_NAME = "CountryDao"
        const val ID = "id"
        const val NAME = "name"
    }
}

data class City(
    val id: Int,
    val name: String,
    val region: Region
) {
    companion object {
        fun mupToCity(
            city: CityDao,
            region: RegionDao?,
            country: CountryDao?
        ) = City(
            id = city.id,
            name = city.name,
            region = Region(
                country = Country(
                    id = country?.id ?: 0,
                    name = country?.name ?: ""
                ),
                id = region?.id ?: 0,
                name = region?.name ?: ""
            )
        )
    }


    fun mapToCityDao() = CityDao(
        id = this.id, name = this.name, regionId = this.region.id, countryId = this.region.country.id

    )
    fun mapToRegionDao() = RegionDao(
        id = this.region.id, name = this.region.name, countryId = this.region.country.id

    )
    fun mapToCountryDao() = CountryDao(
        id = this.region.country.id, name = this.region.country.name
    )
}

data class Region(
    val country: Country,
    val id: Int,
    val name: String
)

data class Country(
    val id: Int,
    val name: String
)



