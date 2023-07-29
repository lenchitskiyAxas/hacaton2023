package ru.aries.hacaton.screens.module_main.main_ribbon

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.aries.hacaton.base.BaseModel
import ru.aries.hacaton.screens.module_main.profile.ProfileScreen
import ru.aries.hacaton.use_case.UseCaseUser

class RibbonModel(
    private val apiUser: UseCaseUser,
) : BaseModel() {

    private val _userData = MutableStateFlow(apiUser.getUserLocalData())
    val userData = _userData.asStateFlow()


    fun goToProfile(){
        getNavigationLevel(NavLevel.MAIN)?.push(ProfileScreen())
    }
}