package ru.aries.hacaton.screens.module_main.profile_redaction

import android.net.Uri
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.data.gDSetLoader
import ru.aries.hacaton.models.api.City
import ru.aries.hacaton.models.api.CreatingFamilyMember
import ru.aries.hacaton.models.api.Gender
import ru.aries.hacaton.models.api.GettingInterest
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.api.UpdatingUser
import ru.aries.hacaton.use_case.UseCaseFamily
import ru.aries.hacaton.use_case.UseCaseInterests
import ru.aries.hacaton.use_case.UseCaseLocations
import ru.aries.hacaton.use_case.UseCaseUser

class ProfileRedactionModel(
    private val apiUser: UseCaseUser,
    private val apiLocation: UseCaseLocations,
    private val apiInterests: UseCaseInterests,
    private val apiFamily: UseCaseFamily,
) : BaseModel()  {

    private var jobSearch: Job = Job()

    private val _userData = MutableStateFlow(apiUser.getUserLocalData())
    val userData = _userData.asStateFlow()

    private val _interests = MutableStateFlow(listOf<GettingInterest>())
    val interests = _interests.asStateFlow()

    private val _locationBirth = MutableStateFlow<City?>(null)
    val locationBirth = _locationBirth.asStateFlow()

    private val _locationResidence = MutableStateFlow<City?>(null)
    val locationResidence = _locationResidence.asStateFlow()

    private val _location = MutableStateFlow(listOf<City>())
    val location = _location.asStateFlow()

    private val _familyError = MutableStateFlow(false)
    val familyError = _familyError.asStateFlow()

    private val _familyMembers = MutableStateFlow(listOf<CreatingFamilyMember>())
    val familyMembers = _familyMembers.asStateFlow()

    init {
        userUpdateAndGetLocations(apiUser.getUserLocalData())
        getInterests()
        getLocation()
    }


    fun getInterests() = coroutineScope.launch {
        _interests.value = apiInterests.getInterests(page = null, name = null).data ?: listOf()
    }

    fun getLocation() = coroutineScope.launch {
        _location.value = apiLocation.getLocationJson()
    }

    fun getFamily() = coroutineScope.launch {
        apiUser.getChooseFamilyId()?.let {
            apiFamily.getFamilyId(
                id = it,
                flowStart = {
                    _familyError.value = false
                    gDSetLoader(true)
                },
                flowSuccess = { family ->
                    gDSetLoader(false)
                    _familyMembers.value = family.mapToCreatingFamilyMember()
                },
                flowError = {
                    _familyError.value = true
                    gDSetLoader(false)
                },
                flowMessage = ::message,
            )
        } ?: run{
            _familyError.value = true
        }
    }

    fun uploadPhoto(image: Uri) = coroutineScope.launch {
        apiUser.postMyAvatar(
            uri = image,
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = {
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
    }

    fun onSearchLocation(searchCity: String?) {
        jobSearch.cancel()
        jobSearch = coroutineScope.launch {
            _location.value = apiLocation.getDdCities(searchCity)
            delay(500)
        }
    }

    fun saveFamily(
        list: List<CreatingFamilyMember>
    ) = coroutineScope.launch {
        apiUser.getChooseFamilyId()?.let {familyId ->
            apiFamily.updateFamilyAndListMember(
                idFamily = familyId,
                listMembers = list,
                flowStart = {
                    gDSetLoader(true)
                },
                flowSuccess = { family ->
                    gDSetLoader(false)
                    _familyMembers.value = family.mapToCreatingFamilyMember()
                },
                flowError = {
                    gDSetLoader(false)
                },
                flowMessage = ::message,
            )
        }
    }

    fun userUpdateAndGetLocations(user: GettingUser) = coroutineScope.launch {
        _userData.value = user
        _locationBirth.value = user.birth_location_id?.let { apiLocation.getDdCity(it) }
        _locationResidence.value = user.location_id?.let { apiLocation.getDdCity(it) }
        logE(_locationBirth.value,_locationResidence.value )
    }

    fun saveContacts(
        locationBirth: City?,
        locationResidence: City?,
        tg: String?,
        tel: String?,
    )= coroutineScope.launch {
        apiUser.putMe(
            user = UpdatingUser().copy(
                tg = tg,
                tel = tel,
                birth_location_id = locationBirth?.id,
                location_id = locationResidence?.id,
            ).getUpdatingUserRequest(userData.value),
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = {
                message(TextApp.textDataUpdated)
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
    }

    fun updateMyPersonData(
        firstName: String?,
        lastName: String?,
        patronymic: String?,
        maidenName: String?,
        gender: Gender?,
        birthdateInMillis: Long?,
    ) = coroutineScope.launch {
        apiUser.putMe(
            user = UpdatingUser().copy(
                first_name = firstName,
                last_name = lastName,
                patronymic = patronymic,
                maiden_name = maidenName,
                gender = gender?.numbGender,
                birthdate = birthdateInMillis?.div(1000)
            ).getUpdatingUserRequest(userData.value),
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = {
                message(TextApp.textDataUpdated)
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
                        },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
    }

    fun updateInterests(
        description: String?,
        interests: List<String>?
    ) = coroutineScope.launch {
        apiUser.putMe(
            user = UpdatingUser().copy(
                description = description,
                interests = interests?.ifEmpty { null },
            ).getUpdatingUserRequest(userData.value),
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = {
                message(TextApp.textDataUpdated)
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
    }

    fun goToPersonalData(){
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionPersonalDataScreen())
    }

    fun goToInterestsData(){
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionInterestsScreen())
    }

    fun goToFamilyData(){
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionFamilyScreen())
    }

    fun goToContactsData(){
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileRedactionContactsScreen())
    }

}