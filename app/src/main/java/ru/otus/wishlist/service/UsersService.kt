package ru.otus.wishlist.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.otus.wishlist.api.models.User
import ru.otus.wishlist.api.models.Wishlist

interface UsersService {

    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>
}