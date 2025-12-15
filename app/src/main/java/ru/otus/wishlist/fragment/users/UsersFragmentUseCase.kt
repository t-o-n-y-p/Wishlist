package ru.otus.wishlist.fragment.users

import android.content.SharedPreferences
import dagger.hilt.android.scopes.ViewModelScoped
import ru.otus.wishlist.api.models.User
import ru.otus.wishlist.fragment.call
import ru.otus.wishlist.service.UsersService
import javax.inject.Inject

@ViewModelScoped
class UsersFragmentUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val usersService: UsersService
) {

    suspend fun getAllUsers(): Result<List<User>> =
        call(sharedPreferences) {
            usersService.getAllUsers()
        }
}