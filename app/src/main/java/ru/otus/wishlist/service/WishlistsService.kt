package ru.otus.wishlist.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.otus.wishlist.api.models.Wishlist
import ru.otus.wishlist.api.models.WishlistDto

interface WishlistsService {

    @GET
    suspend fun getAllWishlists(): Response<List<Wishlist>>

    @POST
    suspend fun createWishlist(@Body requestBody: WishlistDto): Response<Wishlist>

    @GET("{id}")
    suspend fun getWishlist(@Path("id") id: String): Response<Wishlist>

    @PUT("{id}")
    suspend fun updateWishlist(
        @Path("id") id: String, @Body requestBody: WishlistDto
    ): Response<Wishlist>

    @DELETE("{id}")
    suspend fun deleteWishlist(@Path("id") id: String): Response<Unit>
}