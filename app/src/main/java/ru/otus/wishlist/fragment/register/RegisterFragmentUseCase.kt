package ru.otus.wishlist.fragment.register

import android.content.SharedPreferences
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.otus.wishlist.api.models.UserRegistrationDto
import ru.otus.wishlist.fragment.call
import ru.otus.wishlist.service.AuthService
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class RegisterFragmentUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val authService: AuthService
) {

    suspend fun register(username: String, email: String, password: String): Result<Unit> =
        call(sharedPreferences) {
            val dto = UserRegistrationDto(
                username = username,
                email = email,
                password = password
            )
            authService.register(dto)
        }
}