package ru.aries.hacaton.models.api

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.Serializable
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.FieldValidators
import ru.aries.hacaton.screens.module_registration.step_1.StepOneScreen
import ru.aries.hacaton.screens.module_registration.step_2.StepTwoScreen
import ru.aries.hacaton.screens.module_registration.step_3.StepThirdScreen
import ru.aries.hacaton.screens.module_registration.step_4.StepForthScreen
import ru.aries.hacaton.screens.module_registration.step_5.StepFifthScreen

@Serializable
data class GettingUser(
    val first_name: String? = null,
    val last_name: String? = null,
    val patronymic: String? = null,
    val maiden_name: String? = null,
    val gender: Int? = null,
    val tg: String? = null,
    val status: String? = null,
    val birthdate: Long? = null,
    val description: String? = null,
    val bio: String? = null,
    val work: String? = null,
    val favorite_music: String? = null,
    val favorite_movies: String? = null,
    val favorite_books: String? = null,
    val favorite_games: String? = null,
    val interests: List<String> = listOf(),
    val id: Long = 0,
    val email: String? = null,
    val registration_age: Long? = null,
    val tel: String? = null,
    val is_active: Boolean? = null,
    val is_superuser: Boolean? = null,
    val last_visit: Long? = null,
    val location_id: Int? = null,
    val birth_location_id: Int? = null,
    val avatar: String? = null,
) {

    fun getBirthdateInMillis() = if (this.birthdate == null) null else this.birthdate * 1000
    fun getUpdatingUser() = UpdatingUser(
        email = this.email,
        first_name = this.first_name,
        last_name = this.last_name,
        patronymic = this.patronymic,
        maiden_name = this.maiden_name,
        gender = this.gender,
        tg = this.tg,
        tel = this.tel,
        status = this.status,
        birthdate = this.birthdate,
        description = this.description,
        work = this.work,
        favorite_music = this.favorite_music,
        favorite_movies = this.favorite_movies,
        favorite_books = this.favorite_books,
        favorite_games = this.favorite_games,
        interests = this.interests,
        location_id = this.location_id,
        birth_location_id = this.birth_location_id,
    )

    fun getFullName(): String {
        val first = this.first_name?.let { "$it " } ?: ""
        val last = this.last_name?.let { "$it " } ?: ""
        val patr = this.patronymic?.let { "$it " } ?: ""
        val maiden = this.maiden_name?.let { "($it) " } ?: ""

        return first + last + patr + maiden
    }

    fun getNameAndLastName(): String {
        val first = this.first_name?.let { "$it " } ?: ""
        val last = this.last_name?.let { "$it " } ?: ""
        return first + last
    }

    @JsonIgnore
    fun convertInEnumGender(): Gender = Gender.getGenderUser(this.gender)

    @JsonIgnore
    fun stepRegStatus() = when {
        !isStepOneSuccess() -> StepNotEnoughReg.STEP_ONE_NOT_ENOUGH
        !isStepTwoSuccess() -> StepNotEnoughReg.STEP_TWO_NOT_ENOUGH
        !isStepThreeSuccess() -> StepNotEnoughReg.STEP_THIRD_NOT_ENOUGH
        !isStepForthSuccess() -> StepNotEnoughReg.STEP_FORTH_NOT_ENOUGH
        else -> StepNotEnoughReg.STEP_SUCCESS
    }


    fun isStepOneSuccess(
    ) = this.email != null && FieldValidators.isValidEmail(this.email)

    fun isStepForthSuccess(
    ) = this.interests.isNotEmpty() && !description.isNullOrEmpty()

    fun isStepTwoSuccess(
    ) = this.first_name != null && this.last_name != null && this.gender != null

    fun isStepThreeSuccess(
    ) = this.birthdate != null
}

data class UpdatingUser(
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val patronymic: String? = null,
    val maiden_name: String? = null,
    val gender: Int? = null,
    val tg: String? = null,
    val tel: String? = null,
    val status: String? = null,
    val birthdate: Long? = null,
    val description: String? = null,
    val work: String? = null,
    val favorite_music: String? = null,
    val favorite_movies: String? = null,
    val favorite_books: String? = null,
    val favorite_games: String? = null,
    val interests: List<String>? = null,
    val location_id: Int? = null,
    val birth_location_id: Int? = null,
    val password: String? = null,
) {

    fun getUpdatingUserRequest(user: GettingUser) = UpdatingUser(
        email = if (user.email != this.email) this.email else null,
        first_name = if (user.first_name != this.first_name) this.first_name else null,
        last_name = if (user.last_name != this.last_name) this.last_name else null,
        patronymic = if (user.patronymic != this.patronymic) this.patronymic else null,
        maiden_name = if (user.maiden_name != this.maiden_name) this.maiden_name else null,
        gender = if (user.gender != this.gender) this.gender else null,
        tg = if (user.tg != this.tg) this.tg else null,
        tel = if (user.tel != this.tel) this.tel else null,
        status = if (user.status != this.status) this.status else null,
        birthdate = if (user.birthdate != this.birthdate) this.birthdate else null,
        description = if (user.description != this.description) this.description else null,
        work = if (user.work != this.work) this.work else null,
        favorite_music = if (user.favorite_music != this.favorite_music) this.favorite_music else null,
        favorite_movies = if (user.favorite_movies != this.favorite_movies) this.favorite_movies else null,
        favorite_books = if (user.favorite_books != this.favorite_books) this.favorite_books else null,
        favorite_games = if (user.favorite_games != this.favorite_games) this.favorite_games else null,
        interests = if (user.interests != this.interests) this.interests else null,
        location_id = if (user.location_id != this.location_id) this.location_id else null,
        birth_location_id = if (user.birth_location_id != this.birth_location_id) this.birth_location_id else null,
    )

}

@Serializable
data class GettingLocation(
    val name: String,
    val id: Int,
)

data class LoginData(
    val email: String,
    val password: String,
)

data class CreatingEmailVerificationCode(
    val email: String,
)

data class VerifyingTelCode(
    val tel: String,
    val code: String,
)

data class CreatingTelVerificationCode(
    val tel: String,
)

data class GettingTelVerificationCode(
    val code: String,
)

data class VerifyingEmailCode(
    val email: String,
    val code: String,
)

data class SignUpEmail(
    val email: String,
    val password: String,
    val code: String,
)

data class VerificationCode(
    val code: String?,
)

data class GettingInterest(
    val name: String,
    val id: Int,
)

data class TokenWithUser(
    val user: GettingUser,
    val token: String,
)

enum class StepNotEnoughReg {
    STEP_ONE_NOT_ENOUGH,
    STEP_TWO_NOT_ENOUGH,
    STEP_THIRD_NOT_ENOUGH,
    STEP_FORTH_NOT_ENOUGH,
    STEP_FIFTH_NOT_ENOUGH,
    STEP_SUCCESS;

    fun getScreen() = when (this) {
        STEP_ONE_NOT_ENOUGH -> StepOneScreen()
        STEP_TWO_NOT_ENOUGH -> StepTwoScreen()
        STEP_THIRD_NOT_ENOUGH -> StepThirdScreen()
        STEP_FORTH_NOT_ENOUGH -> StepForthScreen()
        STEP_FIFTH_NOT_ENOUGH -> StepFifthScreen()
        else -> StepOneScreen()
    }
}

enum class Gender(val numbGender: Int) {
    WOMAN(1),
    MAN(0);

    companion object {
        fun getGenderUser(idGender: Int?) = when (idGender) {
            1 -> WOMAN
            0 -> MAN
            else -> MAN
        }
    }


    @JsonIgnore
    fun getGenderYourSatellite() = when (this) {
        WOMAN -> MAN
        MAN -> WOMAN
    }


    fun getGenderText() = when (this) {
        WOMAN -> TextApp.textGenderWoman
        MAN -> TextApp.textGenderMan
    }

    fun getGenderTextShort() = when (this) {
        WOMAN -> TextApp.textGenderWomanShort
        MAN -> TextApp.textGenderManShort
    }

    fun getEnterDataYourSatellite() = when (this) {
        WOMAN -> TextApp.textGenderInterMan
        MAN -> TextApp.textGenderInterWoman
    }
}

enum class RoleFamily(val numbRole: Int) {
    SPOUSE(0),
    CHILD(1),
    SELF(2),
    PARENT(3),
    SIBLING(4);

    companion object {
        fun getRoleFamily(idRole: Int?) = when (idRole) {
            0 -> SPOUSE
            1 -> CHILD
            2 -> SELF
            3 -> PARENT
            4 -> SIBLING
            else -> SELF
        }
    }

    fun getTextAddMembers() = when (this) {
        SPOUSE  -> TextApp.textAddSpouse
        CHILD   -> TextApp.textAddChild
        SELF    -> TextApp.textAddSelf
        PARENT  -> TextApp.textAddParent
        SIBLING -> TextApp.textAddSibling
    }
}


data class CreatingFamilyMember(
    val id: Int = 0,
    val first_name: String? = null,
    val last_name: String? = null,
    val patronymic: String? = null,
    val birthdate: Long? = null,
    val maiden_name: String? = null,
    val gender: Int? = null,
    val role: Int? = null,

    ) {
    constructor(
        first_name: String?,
        last_name: String?,
        patronymic: String? = null,
        birthdate: Long?,
        maiden_name: String? = null,
        gender: Gender?,
        role: RoleFamily?,
    ) : this(
        first_name = first_name,
        last_name = last_name,
        patronymic = patronymic,
        birthdate = birthdate,
        maiden_name = maiden_name,
        gender = gender?.numbGender,
        role = role?.numbRole
    )

    fun timeForeSend() = this.copy(birthdate = birthdate?.div(1000))

    @JsonIgnore
    fun getFullName(): String {
        val first = this.first_name?.let { "$it " } ?: ""
        val last = this.last_name?.let { "$it " } ?: ""
        val patr = this.patronymic?.let { "$it " } ?: ""
        val maiden = this.maiden_name?.let { "($it) " } ?: ""

        return first + last + patr + maiden
    }

    @JsonIgnore
    fun isFullForeSend() =
        !first_name.isNullOrEmpty()
                && !last_name.isNullOrEmpty()
                && birthdate != null
                && gender != null
                && role != null


    @JsonIgnore
    fun convertInEnumRoleFamily() = RoleFamily.getRoleFamily(this.role)

    @JsonIgnore
    fun getEnterDataYourSatellite() =
        when (RoleFamily.getRoleFamily(this.role)){
            RoleFamily.SPOUSE -> convertInEnumGender().getGenderTextShort()
            RoleFamily.CHILD -> TextApp.textChildren
            RoleFamily.SELF -> TextApp.formatSomethingYou(convertInEnumGender().getGenderTextShort())
            RoleFamily.PARENT -> TextApp.textGrandParent
            RoleFamily.SIBLING -> TextApp.textBrotherSister
        }



    @JsonIgnore
    fun convertInEnumGender() = Gender.getGenderUser(this.gender)


}

