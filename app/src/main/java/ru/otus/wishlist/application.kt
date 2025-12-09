package ru.otus.wishlist

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import ru.otus.wishlist.service.AuthInterceptor
import ru.otus.wishlist.service.AuthService
import ru.otus.wishlist.service.GiftsService
import ru.otus.wishlist.service.UsersService
import ru.otus.wishlist.service.WishlistsService
import javax.inject.Singleton

@HiltAndroidApp
class WishlistApp : Application()

@Module
@InstallIn(SingletonComponent::class)
class Module {

    private val hostname = "https://api.wishlist.otus.kartushin.su/api/"

    @Provides
    @Singleton
    fun sharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("commonKeyValue", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun converterFactory(): Converter.Factory =
        Json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun authService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): AuthService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("${hostname}auth/")
            .addConverterFactory(converterFactory)
            .build()
            .create(AuthService::class.java)

    @Provides
    @Singleton
    fun usersService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): UsersService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("${hostname}users/")
            .addConverterFactory(converterFactory)
            .build()
            .create(UsersService::class.java)

    @Provides
    @Singleton
    fun wishlistsService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        authInterceptor: AuthInterceptor
    ): WishlistsService =
        Retrofit.Builder()
            .client(okHttpClient.newBuilder().addInterceptor(authInterceptor).build())
            .baseUrl("${hostname}wishlists/")
            .addConverterFactory(converterFactory)
            .build()
            .create(WishlistsService::class.java)

    @Provides
    @Singleton
    fun giftsService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        authInterceptor: AuthInterceptor
    ): GiftsService =
        Retrofit.Builder()
            .client(okHttpClient.newBuilder().addInterceptor(authInterceptor).build())
            .baseUrl("${hostname}gifts/")
            .addConverterFactory(converterFactory)
            .build()
            .create(GiftsService::class.java)
}