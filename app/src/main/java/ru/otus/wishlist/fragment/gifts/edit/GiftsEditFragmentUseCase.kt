package ru.otus.wishlist.fragment.gifts.edit

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.api.models.Gift
import ru.otus.wishlist.api.models.GiftDto
import ru.otus.wishlist.api.models.Wishlist
import ru.otus.wishlist.api.models.WishlistDto
import ru.otus.wishlist.service.GiftsService
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class GiftsEditFragmentUseCase @Inject constructor(
    private val giftsService: GiftsService
) {

    suspend fun updateGift(id: String, name: String, description: String, price: Int): Result<Gift> =
        runCatching {
            val dto = GiftDto(
                name = name,
                description = description,
                price = price.toDouble()
            )
            val response = withContext(Dispatchers.IO) {
                giftsService.updateGift(id, dto)
            }
            when {
                response.code() == 200 -> response.body() ?: throw IOException()
                else -> throw SecurityException()
            }
        }

    suspend fun createGift(wishlistId: String, name: String, description: String, price: Int): Result<Gift> =
        runCatching {
            val dto = GiftDto(
                name = name,
                description = description,
                price = price.toDouble()
            )
            val response = withContext(Dispatchers.IO) {
                giftsService.createGift(wishlistId, dto)
            }
            when {
                response.code() == 200 -> response.body() ?: throw IOException()
                else -> throw SecurityException()
            }
        }
}