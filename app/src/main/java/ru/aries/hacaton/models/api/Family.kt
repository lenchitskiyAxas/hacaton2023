package ru.aries.hacaton.models.api

data class GettingFamilyRequest(
    val id: Int,
    val is_approved: Boolean?,
    val user: GettingUser?,
)


data class UpdatingFamilyRequest(
    val is_approved: Boolean
)


data class BodyAny(
    val any: Any? = null
)

data class UpdatingFamily(
    val name: String?
)


data class GettingFamily(
    val id: Int,
    val name: String?,
    val members: List<GettingFamilyMember>,
) {


    fun getNameFamily(): String {
        this.name?.let {
            return it
        }
        val member = this.members.firstOrNull { it.convertUserRoleInEnum() == RoleFamily.SELF }
        return "${member?.first_name ?: ""}#${member?.id ?: ""}"
    }


    fun mapToCreatingFamilyMember() = this.members.map {
        CreatingFamilyMember(
            id = it.id,
            first_name = it.first_name,
            last_name = it.last_name,
            patronymic = it.patronymic,
            birthdate = it.birthdate,
            maiden_name = it.maiden_name,
            gender = it.gender,
            role = it.user_role
        )
    }
}


data class GettingFamilyMember(
    val id: Int,
    val first_name: String?,
    val last_name: String?,
    val patronymic: String?,
    val birthdate: Long?,
    val maiden_name: String?,
    val gender: Int?,
    val user: GettingUser?,
    val owner_role: Int?,
    val user_role: Int?,
) {

    fun convertGenderInEnum() = Gender.getGenderUser(this.gender)
    fun convertOwnerRoleInEnum() = RoleFamily.getRoleFamily(this.owner_role)
    fun convertUserRoleInEnum() = RoleFamily.getRoleFamily(this.user_role)


}
