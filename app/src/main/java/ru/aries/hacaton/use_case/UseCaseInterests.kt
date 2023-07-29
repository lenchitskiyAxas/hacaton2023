package ru.aries.hacaton.use_case

import android.content.Context
import ru.aries.hacaton.R
import ru.aries.hacaton.base.extension.jsonToObject
import ru.aries.hacaton.base.util.readJsonFromRaw
import ru.aries.hacaton.data.GlobalDada
import ru.aries.hacaton.data.api_client.ApiInterests
import ru.aries.hacaton.data.api_client.ApiLocation
import ru.aries.hacaton.data.dao.CityDaoRoom
import ru.aries.hacaton.data.gDGetCities
import ru.aries.hacaton.data.gDGetCity
import ru.aries.hacaton.data.gDSetCities
import ru.aries.hacaton.models.api.City
import ru.aries.hacaton.models.api.GettingInterest
import ru.aries.hacaton.models.api.GettingLocation
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.api.ListCity
import ru.aries.hacaton.models.api.RudApi

class UseCaseInterests(
    private val apiInterests: ApiInterests,
) {

    suspend fun getInterests(
        page: Int? = 1,
        name: String? = null,
    ): RudApi<List<GettingInterest>> = apiInterests.getInterests(
        name = name,
        page = page
    )

}