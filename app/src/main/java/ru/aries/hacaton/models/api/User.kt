package ru.aries.hacaton.models.api

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.Serializable
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.FieldValidators
@Serializable
data class GettingUser(
    val email: String? = null,
    val tel: String? = null,
    val is_active: Boolean? = null,
    val is_superuser: Boolean? = null,

    val avatar: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val patronymic: String? = null,
    val fields: GettingFields? = null,
    val birthdate: Long? = null,
    val gender: Int? = null,
    val id: Long = 0,
    val last_activity: Long? = null,


) {

    fun getBirthdateInMillis() = if (this.birthdate == null) null else this.birthdate * 1000
    fun getUpdatingUser() = UpdatingUser(
        email = this.email,
        first_name = this.first_name,
        last_name = this.last_name,
        patronymic = this.patronymic,
        gender = this.gender,
        tel = this.tel,
        birthdate = this.birthdate,

    )

    fun getFullName(): String {
        val first = this.first_name?.let { "$it " } ?: ""
        val last = this.last_name?.let { "$it " } ?: ""
        val patr = this.patronymic?.let { "$it " } ?: ""

        return first + last + patr
    }

    fun getNameAndLastName(): String {
        val first = this.first_name?.let { "$it " } ?: ""
        val last = this.last_name?.let { "$it " } ?: ""
        return first + last
    }

    @JsonIgnore
    fun convertInEnumGender(): Gender = Gender.getGenderUser(this.gender)

    fun isStepOneSuccess(
    ) = this.email != null && FieldValidators.isValidEmail(this.email)

    fun isStepForthSuccess(
    ) = true

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
@Serializable
data class GettingFields(
    val name: String?,
    val url: String?,
    val description: String?,
    val min_mark: Int?,
    val price: Int?,
    val id: Int,
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
data class GettingOffer(
    val title:String?,
    val min_price:Int?,
    val id:Int,
    val max_price:Int?,
    val percent:Int?,
    val annual_payment:Int?,
    val payment_term:Int?,
    val bank:GettingBank?,
    )

data class GettingBank(
    val name:String?,
    val url:String?,
    val icon:String?,
    val id:Int)


data class GettingBidByBank(
val desired_amount: Int?,
val id: Int,
val is_accepted: Boolean?,
val actual_amount: Int?,
val annual_payment: Int?,
val percent: Int?,
val created: Long?,
val offer: GettingOffer?,
val user: GettingUser?,

)

data class  BidApproval(
    val actual_amount: Int,
    val annual_payment: Int,
    val percent: Int,

    )
data class  BidCallbackBody(
    val is_accepted: Boolean,
    val approval: BidApproval,

)
data class  BidApprovalResponse(
    val code: Int?,
    val error: String?,
    val bid: GettingBidByBank,

)