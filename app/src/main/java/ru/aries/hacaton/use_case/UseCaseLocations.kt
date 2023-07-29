package ru.aries.hacaton.use_case

import android.content.Context
import ru.aries.hacaton.R
import ru.aries.hacaton.base.extension.jsonToObject
import ru.aries.hacaton.base.util.readJsonFromRaw
import ru.aries.hacaton.data.GlobalDada
import ru.aries.hacaton.data.api_client.ApiLocation
import ru.aries.hacaton.data.dao.CityDaoRoom
import ru.aries.hacaton.data.gDGetCities
import ru.aries.hacaton.data.gDGetCity
import ru.aries.hacaton.data.gDSetCities
import ru.aries.hacaton.models.api.City
import ru.aries.hacaton.models.api.GettingLocation
import ru.aries.hacaton.models.api.ListCity
import ru.aries.hacaton.models.api.RudApi

class UseCaseLocations(
    private val apiLocation: ApiLocation,
    private val daoLocation: CityDaoRoom,
    private val context: Context
) {

    suspend fun initDdCities(inSuccess:()-> Unit){
        if (daoLocation.isEmptyCityDao()) {
            val locations = readJsonFromRaw(context, R.raw.cities)
            val cities: List<City> = locations.jsonToObject<ListCity>()?.data ?: listOf()
            daoLocation.setCitiesData(cities)
        }
        inSuccess.invoke()
    }


    suspend fun getDdCities(
        name: String? = null,
    ): List<City> = daoLocation.getCitiesLimit(name)

    suspend fun getDdCity(
        id: Int,
    ): City? = daoLocation.getCity(id)

    suspend fun getLocationsApi(
        page: Int = 1,
        name: String? = null,
    ): RudApi<List<GettingLocation>> = apiLocation.getLocations(
        name = name,
        page = page
    )

    suspend fun getLocationJson(name: String? = null): List<City> {
        GlobalDada.value?.listCities?.ifEmpty {
            val locations = readJsonFromRaw(context, R.raw.cities)
            val cities: List<City> = locations.jsonToObject<ListCity>()?.data ?: listOf()
            gDSetCities(cities)
        }

        return   gDGetCities(name)
    }

    suspend fun getLocationJson(id: Int): City? {
        GlobalDada.value?.listCities?.ifEmpty {
            val locations = readJsonFromRaw(context, R.raw.cities)
            val cities: List<City> = locations.jsonToObject<ListCity>()?.data ?: listOf()
            gDSetCities(cities)
        }
        return   gDGetCity(id)
    }
}