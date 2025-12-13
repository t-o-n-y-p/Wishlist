package ru.otus.wishlist.fragment.wishlists.edit

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.api.models.Wishlist
import ru.otus.wishlist.api.models.WishlistDto
import ru.otus.wishlist.service.WishlistsService
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class WishlistsEditFragmentUseCase @Inject constructor(
    private val wishlistsService: WishlistsService
) {

    suspend fun updateWishlist(id: String, title: String, description: String): Result<Wishlist> =
        runCatching {
            val dto = WishlistDto(
                title = title,
                description = description
            )
            val response = withContext(Dispatchers.IO) {
                wishlistsService.updateWishlist(id, dto)
            }
            when {
                response.code() == 200 -> response.body() ?: throw IOException()
                else -> throw SecurityException()
            }
        }

    suspend fun createWishlist(title: String, description: String): Result<Wishlist> =
        runCatching {
            val dto = WishlistDto(
                title = title,
                description = description
            )
            val response = withContext(Dispatchers.IO) {
                wishlistsService.createWishlist(dto)
            }
            when {
                response.code() == 200 -> response.body() ?: throw IOException()
                else -> throw SecurityException()
            }
        }
}