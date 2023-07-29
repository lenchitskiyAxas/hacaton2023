package ru.aries.hacaton.models.local

import kotlinx.serialization.Serializable
import ru.aries.hacaton.models.api.GettingUser

@Serializable
data class LocalAppData(
    val user: GettingUser? = null,
    val chooserFamilyId: Int? = null,
    val token:String? = null,
    val tokenFirebase:String? = null,
)


