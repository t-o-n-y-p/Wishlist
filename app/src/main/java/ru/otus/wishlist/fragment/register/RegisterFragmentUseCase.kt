package ru.otus.wishlist.fragment.register

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.api.models.UserRegistrationDto
import ru.otus.wishlist.service.AuthService
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class RegisterFragmentUseCase @Inject constructor(
    private val authService: AuthService
) {

    suspend fun register(username: String, email: String, password: String): Result<Unit> =
        runCatching {
            val dto = UserRegistrationDto(
                username = username,
                email = email,
                password = password
            )
            val response = withContext(Dispatchers.IO) {
                authService.register(dto)
            }
            when {
                response.code() == 200 -> response.body() ?: throw IOException()
                else -> throw SecurityException()
            }
        }
}