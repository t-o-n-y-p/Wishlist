package ru.otus.wishlist.fragment.wishlists

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class WishlistsData(
    val id: String,
    val username: String
) : Parcelable
