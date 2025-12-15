package ru.otus.wishlist.fragment.wishlists

import android.content.SharedPreferences
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.api.models.Wishlist
import ru.otus.wishlist.fragment.call
import ru.otus.wishlist.service.WishlistsService
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class WishlistsFragmentUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val wishlistsService: WishlistsService
) {

    suspend fun getAllWishlists(): Result<List<Wishlist>> =
        call(sharedPreferences) {
            wishlistsService.getAllWishlists()
        }

    suspend fun deleteWishlist(id: String): Result<Unit> =
        call(sharedPreferences) {
            wishlistsService.deleteWishlist(id = id)
        }

    suspend fun getUserWishlists(userId: String): Result<List<Wishlist>> =
        call(sharedPreferences) {
            wishlistsService.getUserWishlists(userId = userId)
        }
}