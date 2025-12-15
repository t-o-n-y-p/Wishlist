package ru.otus.wishlist.fragment.login

import android.content.SharedPreferences
import dagger.hilt.android.scopes.ViewModelScoped
import ru.otus.wishlist.api.models.Login
import ru.otus.wishlist.api.models.LoginDto
import ru.otus.wishlist.fragment.call
import ru.otus.wishlist.service.AuthService
import javax.inject.Inject

@ViewModelScoped
class LoginFragmentUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val authService: AuthService
) {

    suspend fun login(username: String, password: String): Result<Login> =
        call(sharedPreferences) {
            val dto = LoginDto(
                username = username,
                password = password
            )
            authService.login(dto)
        }
}