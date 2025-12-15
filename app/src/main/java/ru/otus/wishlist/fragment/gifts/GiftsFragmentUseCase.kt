package ru.otus.wishlist.fragment.gifts

import android.content.SharedPreferences
import dagger.hilt.android.scopes.ViewModelScoped
import ru.otus.wishlist.api.models.Gift
import ru.otus.wishlist.fragment.call
import ru.otus.wishlist.service.GiftsService
import javax.inject.Inject

@ViewModelScoped
class GiftsFragmentUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val giftsService: GiftsService
) {

    suspend fun deleteGift(id: String): Result<Unit> =
        call(sharedPreferences) {
            giftsService.deleteGift(id = id)
        }

    suspend fun reserveGift(id: String): Result<Gift> =
        call(sharedPreferences) {
            giftsService.reserveGift(id = id)
        }
}