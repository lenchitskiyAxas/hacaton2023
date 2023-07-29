package ru.aries.hacaton.models.api

data class GettingWish(
    val title: String?,
    val description: String?,
    val price: Int?,
    val link: String?,
    val id: Int,
    val cover: String?,
    val created: Long,
    val is_fulfilled: Int?,
    val user: GettingUser,
    val wishlist: GettingWishShort?,
){

    fun getStatusFulfilled() = StatusFulfilled.getNew(is_fulfilled)


}

data class  GettingWishlist(
    val title: String?,
    val is_secret: Boolean?,
    val id: Int,
    val created: Long,
    val cover: String?,
    val wishes: List<GettingWish>?,
)

data class  CreatingWishlist(
    val title: String?= null,
    val is_secret: Boolean?= null,
    val author_ids: List<Int>? = null
)

data class CreatingWish(
    val title: String? = null,
    val description: String? = null,
    val price: Int? = null,
    val link: String? = null,
    val wishlist_id: Int? = null,
)

data class GettingWishShort(
    val id: Int,
    val title: String,
)

/**
 * new = 0
 *
 * progress = 1
 *
 * fulfilled  = 2
 */
enum class StatusFulfilled(){
    NEW,
    PROGRESS,
    FULFILLED;
    companion object {
        fun getNew(num: Int?) = when(num){
            0-> NEW
            1-> PROGRESS
            2->  FULFILLED
            else -> NEW
        }
    }
}