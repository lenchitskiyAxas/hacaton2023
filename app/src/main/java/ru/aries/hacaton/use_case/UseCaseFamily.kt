package ru.aries.hacaton.use_case

import io.ktor.http.isSuccess
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.data.api_client.ApiFamily
import ru.aries.hacaton.models.api.CreatingFamilyMember
import ru.aries.hacaton.models.api.GettingFamily

class UseCaseFamily(
    private val apiFamily: ApiFamily,
) {

    suspend fun getMyFamily(
        flowStart: () -> Unit = {},
        flowSuccess: (List<GettingFamily>) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ){
        flowStart.invoke()

        val response = apiFamily.getFamilies()

        if (!response.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getMyFamily",response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getMyFamily",response)
        }
    }


    suspend fun getFamilyId(
        id:Int,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingFamily) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ){
        flowStart.invoke()

        val response = apiFamily.getFamilyId(id)

        if (!response.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getFamilyId",response)
            return
        }

        response.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(response.getDescriptionRudApi())
            logE("getFamilyId",response)
        }
    }

    suspend fun postNewFamilyAndListMember(
        listMembers: List<CreatingFamilyMember>,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingFamily) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
       val newFamily =  apiFamily.postCreateFamily(null)
        if (!newFamily.status.isSuccess()) {
            flowError.invoke()
            flowMessage.invoke(newFamily.response.getDescriptionRudApi())
            logE("postNewFamilyAndListMember",newFamily)
            return
        }
        val idFamily = newFamily.response.data?.id ?: run{
            flowError.invoke()
            flowMessage.invoke(TextApp.textSomethingWentWrong)
            return
        }

        listMembers.forEach { familyMember->
            val call = apiFamily.postCreateFamilyMember(
                familyId = idFamily,
                familyMember = familyMember.timeForeSend()
            )

            if (!call.status.isSuccess()) {
                flowMessage.invoke(call.response.getDescriptionRudApi())
                logE("postNewFamilyAndListMember",call)
                return
            }
        }

        val family =  apiFamily.getFamilyId(familyId = idFamily)
        if (!family.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(family.getDescriptionRudApi())
            logE("postNewFamilyAndListMember",family)
            return
        }

        family.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(family.getDescriptionRudApi())
            logE("postNewFamilyAndListMember",family)
        }
    }


    suspend fun updateFamilyAndListMember(
        idFamily:Int,
        listMembers: List<CreatingFamilyMember>,
        flowStart: () -> Unit = {},
        flowSuccess: (GettingFamily) -> Unit,
        flowError: () -> Unit = {},
        flowMessage: (String) -> Unit = {},
    ) {
        flowStart.invoke()
        listMembers.forEach { familyMember->
            if (familyMember.id == 0){
                val responseNewMember = apiFamily.postCreateFamilyMember(
                    familyId = idFamily,
                    familyMember = familyMember.timeForeSend()
                )

                if (!responseNewMember.status.isSuccess()) {
                    flowMessage.invoke(responseNewMember.response.getDescriptionRudApi())
                    logE("updateFamilyAndListMember",responseNewMember)
                    return
                }
            }

            if (familyMember.id > 0){
                val responseUpdateMember = apiFamily.putUpdateFamilyMember(
                    memberId = familyMember.id,
                    familyMember = familyMember.timeForeSend()
                )
                if (!responseUpdateMember.isSuccess) {
                    flowMessage.invoke(responseUpdateMember.getDescriptionRudApi())
                    logE("updateFamilyAndListMember",responseUpdateMember)
                    return
                }
            }
        }

        val family =  apiFamily.getFamilyId(familyId = idFamily)
        if (!family.isSuccess) {
            flowError.invoke()
            flowMessage.invoke(family.getDescriptionRudApi())
            logE("updateFamilyAndListMember",family)
            return
        }

        family.data?.let { newData ->
            flowSuccess.invoke(newData)
        } ?: run {
            flowError.invoke()
            flowMessage.invoke(family.getDescriptionRudApi())
            logE("updateFamilyAndListMember",family)
        }
    }
}