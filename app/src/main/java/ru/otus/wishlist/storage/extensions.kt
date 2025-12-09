package ru.otus.wishlist.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.serializer

inline fun <reified T> SharedPreferences.get(): T? =
    getString(T::class.simpleName, "{}")?.let { Json.decodeFromString<T>(it) }

inline fun <reified T> SharedPreferences.put(value: T) =
    edit {
        putString(
            T::class.simpleName,
            Json.encodeToString(EmptySerializersModule().serializer(), value))
    }

inline fun <reified T> SharedPreferences.cleanup() =
    edit {
        putString(T::class.simpleName, null)
    }
