package ru.otus.wishlist.fragment.mine

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.api.models.Wishlist
import ru.otus.wishlist.service.WishlistsService
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class MineFragmentUseCase @Inject constructor(
    private val wishlistsService: WishlistsService
) {

    suspend fun getAllWishlists(): Result<List<Wishlist>> =
        runCatching {
            val response = withContext(Dispatchers.IO) {
                wishlistsService.getAllWishlists()
            }
            when {
                response.code() == 200 -> response.body() ?: throw IOException()
                else -> throw SecurityException()
            }
        }
}