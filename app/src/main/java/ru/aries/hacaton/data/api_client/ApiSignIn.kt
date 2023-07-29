package ru.aries.hacaton.data.api_client


import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.models.api.BidApprovalResponse
import ru.aries.hacaton.models.api.BidCallbackBody
import ru.aries.hacaton.models.api.GettingBidByBank
import ru.aries.hacaton.models.api.GettingOffer
import ru.aries.hacaton.models.api.LoginData
import ru.aries.hacaton.models.api.RudApi
import ru.aries.hacaton.models.api.TokenWithUser

class ApiSignIn(
    private val client: Client,
) {

    /**Войти По Логину И Паролю
     * RudApi [TokenWithUser]
     * */
    suspend fun postSignIn(
        email: String,
        password: String,
    ) = client.api.postRequest<LoginData, TokenWithUser>(
        urlString = "/api/sign-in/email-password/",
        body = LoginData(email = email, password = password)
    )

    suspend fun getBids(): RudApi<List<GettingBidByBank>> {
        return try {
            val response = client.api.get(urlString = "/api/cp/bids/")
            response.body<RudApi<List<GettingBidByBank>>>().copy(
                isSuccess = response.status.isSuccess()
            )
        } catch (e: Exception) {
            logE("getBids", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    suspend fun getOffers(): RudApi<List<GettingOffer>> {
        return try {
            val response = client.api.get(urlString = "/api/cp/offers/")
            response.body<RudApi<List<GettingOffer>>>().copy(
                isSuccess = response.status.isSuccess()
            )
        } catch (e: Exception) {
            logE("getOffers", e.message)
            e.printStackTrace()
            RudApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    suspend fun postBidId(
        bid: BidCallbackBody,
        bidId: Int,
    ) = client.api.postRequest<BidCallbackBody, BidApprovalResponse>(
        urlString = "/api/cp/bids/${bidId}/process/",
        body = bid
    )

}