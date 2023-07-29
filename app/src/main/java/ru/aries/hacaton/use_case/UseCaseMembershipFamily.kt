package ru.aries.hacaton.use_case

import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logD
import ru.aries.hacaton.data.api_client.ApiRequestsFamily
import ru.aries.hacaton.models.api.GettingFamilyRequest

class UseCaseMembershipFamily(
    private val apiRequestsFamily: ApiRequestsFamily,
) {


    suspend fun postFamiliesRequests(
        familyId: Int,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingFamilyRequest) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        val data = apiRequestsFamily.postFamiliesRequests(familyId)
        if (!data.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
            return
        }
        logD("postFamiliesRequests", data)
        data.response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(data.response.getDescriptionRudApi())
        }
    }

}