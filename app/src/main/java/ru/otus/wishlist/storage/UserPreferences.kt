package ru.otus.wishlist.storage

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    var token: String = "",
    var name: String = ""
) {

    fun getAuthHeaderValue() = "Bearer $token"

    fun isLoggedIn() = token.isNotBlank()
}
