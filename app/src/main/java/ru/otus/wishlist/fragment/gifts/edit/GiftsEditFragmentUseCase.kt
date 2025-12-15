package ru.otus.wishlist.fragment.gifts.edit

import android.content.SharedPreferences
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.api.models.Gift
import ru.otus.wishlist.api.models.GiftDto
import ru.otus.wishlist.api.models.Wishlist
import ru.otus.wishlist.api.models.WishlistDto
import ru.otus.wishlist.fragment.call
import ru.otus.wishlist.service.GiftsService
import java.io.IOException
import javax.inject.Inject
import kotlin.text.toDouble

@ViewModelScoped
class GiftsEditFragmentUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val giftsService: GiftsService
) {

    suspend fun updateGift(id: String, name: String, description: String, price: Int): Result<Gift> =
        call(sharedPreferences) {
            val dto = GiftDto(
                name = name,
                description = description,
                price = price.toDouble()
            )
            giftsService.updateGift(id, dto)
        }

    suspend fun createGift(wishlistId: String, name: String, description: String, price: Int): Result<Gift> =
        call(sharedPreferences) {
            val dto = GiftDto(
                name = name,
                description = description,
                price = price.toDouble()
            )
            giftsService.createGift(wishlistId, dto)
        }
}