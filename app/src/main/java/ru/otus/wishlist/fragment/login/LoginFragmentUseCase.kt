package ru.otus.wishlist.fragment.login

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.api.models.Login
import ru.otus.wishlist.api.models.LoginDto
import ru.otus.wishlist.service.AuthService
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class LoginFragmentUseCase @Inject constructor(
    private val authService: AuthService
) {

    suspend fun login(username: String, password: String): Result<Login> =
        runCatching {
            val dto = LoginDto(
                username = username,
                password = password
            )
            val response = withContext(Dispatchers.IO) {
                authService.login(dto)
            }
            when {
                response.code() == 200 -> response.body() ?: throw IOException()
                else -> throw SecurityException()
            }
        }
}