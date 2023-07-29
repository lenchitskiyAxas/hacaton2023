package ru.aries.hacaton.screens.module_registration

import android.net.Uri
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.base.extension.onlyDigit
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.util.logD
import ru.aries.hacaton.data.gDSetLoader
import ru.aries.hacaton.models.api.City
import ru.aries.hacaton.models.api.CreatingFamilyMember
import ru.aries.hacaton.models.api.Gender
import ru.aries.hacaton.models.api.GettingFamilyRequest
import ru.aries.hacaton.models.api.GettingInterest
import ru.aries.hacaton.models.api.GettingUser
import ru.aries.hacaton.models.api.RoleFamily
import ru.aries.hacaton.models.api.UpdatingUser
import ru.aries.hacaton.screens.module_authorization.AuthScreen
import ru.aries.hacaton.screens.module_registration.step_2.StepTwoScreen
import ru.aries.hacaton.screens.module_registration.step_3.StepThirdScreen
import ru.aries.hacaton.screens.module_registration.step_4.StepForthScreen
import ru.aries.hacaton.screens.module_registration.step_5.StepFifthScreen
import ru.aries.hacaton.screens.module_registration.step_5_join_cell_1.StepFifthJoinInCellFirst
import ru.aries.hacaton.screens.module_registration.step_5_join_cell_2.StepFifthJoinInCellSecond
import ru.aries.hacaton.screens.module_registration.step_5_new_cell_1.StepFifthCreateNewCellFirst
import ru.aries.hacaton.screens.module_registration.step_5_new_cell_2.StepFifthCreateNewCellSecond
import ru.aries.hacaton.screens.module_registration.step_5_new_cell_3.StepFifthCreateNewCellThird
import ru.aries.hacaton.screens.splash.SplashScreen
import ru.aries.hacaton.use_case.UseCaseFamily
import ru.aries.hacaton.use_case.UseCaseInterests
import ru.aries.hacaton.use_case.UseCaseLocations
import ru.aries.hacaton.use_case.UseCaseMembershipFamily
import ru.aries.hacaton.use_case.UseCaseSignIn
import ru.aries.hacaton.use_case.UseCaseUser

class RegStepsModel(
    private val apiSignIn: UseCaseSignIn,
    private val apiUser: UseCaseUser,
    private val apiLocation: UseCaseLocations,
    private val apiInterests: UseCaseInterests,
    private val apiMembershipFamily: UseCaseMembershipFamily,
    private val apiFamily: UseCaseFamily
) : BaseModel() {

    private var jobSearch: Job = Job()
    private var jobSearchInterests: Job = Job()

    private val _userData = MutableStateFlow(apiSignIn.getUserLocalData())
    val userData = _userData.asStateFlow()

    private val _locationBirth = MutableStateFlow<City?>(null)
    val locationBirth = _locationBirth.asStateFlow()

    private val _locationResidence = MutableStateFlow<City?>(null)
    val locationResidence = _locationResidence.asStateFlow()

    private val _email = MutableStateFlow(_userData.value.email ?: "")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _location = MutableStateFlow(listOf<City>())
    val location = _location.asStateFlow()

    private val _interests = MutableStateFlow(listOf<GettingInterest>())
    val interests = _interests.asStateFlow()

    private val _familyRequest = MutableStateFlow<GettingFamilyRequest?>(null)
    val familyRequest = _familyRequest.asStateFlow()

    private val _codeValidation = MutableStateFlow<String?>(null)
    val codeValidation = _codeValidation.asStateFlow()


    private val _firstFamilyMember = MutableStateFlow(CreatingFamilyMember(
        role = RoleFamily.SPOUSE.numbRole,
        gender = _userData.value.convertInEnumGender().getGenderYourSatellite().numbGender))
    val firstFamilyMember = _firstFamilyMember.asStateFlow()

    private val _familyMembers = MutableStateFlow(listOf<CreatingFamilyMember>())
    val familyMembers = _familyMembers.asStateFlow()

    init {
        coroutineScope.launch {
            _location.value = apiLocation.getLocationJson()
            _interests.value = apiInterests.getInterests(page = 1, name = null).data ?: listOf()
        }
    }


    fun onSearchInterests(search: String) {
        jobSearchInterests.cancel()
        jobSearchInterests = coroutineScope.launch {
            delay(500)
            _interests.value = apiInterests.getInterests(
                page = 1,
                name = search.ifEmpty { null }).data ?: listOf()
        }
    }


    fun deleteCodeValidation() {
        _codeValidation.value = null
    }

    fun updateMyProfileStepThird(
        birthdate: Long,
        residenceLocation: City?,
        birthLocation: City?
    ) = coroutineScope.launch {
        apiUser.putMe(
            user = UpdatingUser().copy(
                birthdate = birthdate.div(1000),
                birth_location_id = birthLocation?.id,
                location_id = residenceLocation?.id,
            ).getUpdatingUserRequest(userData.value),
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = {
                message(TextApp.textDataUpdated)
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
//                goToForthStep()
                goToFifthStepIfEmpty()
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

    suspend fun getNameLocation(id: Int): City? = apiLocation.getLocationJson(id)

    fun userUpdateAndGetLocations(user: GettingUser) = coroutineScope.launch {
        _userData.value = user
        _firstFamilyMember.value = CreatingFamilyMember(
            role = RoleFamily.SPOUSE.numbRole,
            gender = _userData.value.convertInEnumGender().getGenderYourSatellite().numbGender)
        _locationBirth.value = user.location_id?.let { apiLocation.getDdCity(it) }
        _locationResidence.value = user.birth_location_id?.let { apiLocation.getDdCity(it) }
    }

    fun updateFirstFamilyMember(person: CreatingFamilyMember) {
        _firstFamilyMember.value = person
    }

    fun updateListFamilyMembers(persons: List<CreatingFamilyMember>) {
        _familyMembers.value = persons
    }

    fun addFamilyMember() {
        _familyMembers.value =
            _familyMembers.value + CreatingFamilyMember(role = RoleFamily.CHILD.numbRole)
    }

    fun updateMyProfileStepTwo(
        firstName: String,
        lastName: String,
        patronymic: String?,
        maidenName: String?,
        gender: Gender,
    ) = coroutineScope.launch {
        apiUser.putMe(
            user = UpdatingUser().copy(
                first_name = firstName,
                last_name = lastName,
                patronymic = patronymic,
                maiden_name = maidenName,
                gender = gender.numbGender
            ).getUpdatingUserRequest(userData.value),
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = {
                message(TextApp.textDataUpdated)
                gDSetLoader(false)
                userUpdateAndGetLocations(it)
                goToThirdStep()
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
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


    fun goToTwoStep(enterCode: String) {
        if (_codeValidation.value != enterCode) {
            message(TextApp.errorInvalidCodeEntered)
            return
        }
        coroutineScope.launch {
            apiSignIn.postReg(
                email = email.value,
                password = password.value,
                code = enterCode,
                flowStart = { gDSetLoader(true) },
                flowSuccess = {
                    gDSetLoader(false)
                    userUpdateAndGetLocations(it)
                    navigator.push(StepTwoScreen())
                },
                flowError = {
                    deleteCodeValidation()
                    gDSetLoader(false)
                },
                flowMessage = ::message
            )
        }
    }

    fun createNewEmail(email: String, password: String) = coroutineScope.launch {
        _email.value = email
        _password.value = password
        apiSignIn.postEmail(
            email = email,
            flowStart = { gDSetLoader(true) },
            flowSuccess = {
                gDSetLoader(false)
                _codeValidation.value = it.code
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
    }

    fun goToThirdStep() {
        navigator.push(StepThirdScreen())
    }

    fun goToAuth() {
        getNavigationLevel(NavLevel.MAIN)?.replaceAll(AuthScreen())
    }

    fun goToForthStep() {
        navigator.push(StepForthScreen())
    }


    fun goToJoinInCellFirst() {
        navigator.push(StepFifthJoinInCellFirst())
    }

    fun goToNewCellThird() {
        navigator.push(StepFifthCreateNewCellThird())
    }

    fun goToCreateNewCellFirst() {
        navigator.push(StepFifthCreateNewCellFirst())
    }

    fun goToFifthStepIfEmpty() {
        navigator.push(StepFifthScreen())
    }

    fun addSatelliteInCell() {
        logD("addSatelliteInCell")
        navigator.push(StepFifthCreateNewCellSecond())
    }

    fun goBackToSplashScreen() {
        getNavigationLevel(NavLevel.MAIN)?.replaceAll(SplashScreen())
    }

    fun goToFifthStep(
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
                navigator.push(StepFifthScreen())
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
    }

    fun finishAddNewFamilyCell() = coroutineScope.launch {
        val listMembers = familyMembers.value + firstFamilyMember.value
        apiFamily.postNewFamilyAndListMember(
            listMembers = listMembers,
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = {
                gDSetLoader(false)
                goBackToSplashScreen()
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
    }

    fun sendIdCell(
        id: String,
    ) = coroutineScope.launch {
        val idNumb = id.onlyDigit().toIntOrNull() ?: return@launch
        apiMembershipFamily.postFamiliesRequests(
            familyId = idNumb,
            flowStart = {
                gDSetLoader(true)
            },
            flowSuccess = {
                gDSetLoader(false)
                _familyRequest.value = it
                navigator.push(StepFifthJoinInCellSecond())
            },
            flowError = {
                gDSetLoader(false)
            },
            flowMessage = ::message
        )
    }
}