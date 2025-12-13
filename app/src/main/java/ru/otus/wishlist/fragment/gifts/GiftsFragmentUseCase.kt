package ru.otus.wishlist.fragment.gifts

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.service.GiftsService
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class GiftsFragmentUseCase @Inject constructor(
    private val giftsService: GiftsService
) {

    suspend fun deleteGift(id: String): Result<Unit> =
        runCatching {
            val response = withContext(Dispatchers.IO) {
                giftsService.deleteGift(id = id)
            }
            when {
                response.code() == 200 -> response.body() ?: throw IOException()
                else -> throw SecurityException()
            }
        }
}