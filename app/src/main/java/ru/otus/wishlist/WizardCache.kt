package ru.otus.wishlist

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ru.otus.wishlist.api.models.User
import ru.otus.wishlist.recyclerview.gifts.GiftsItem
import ru.otus.wishlist.recyclerview.wishlists.WishlistsItem
import javax.inject.Inject

@ActivityRetainedScoped
class WizardCache @Inject constructor() {

    var currentUser: User? = null
    var wishlists: MutableList<WishlistsItem> = mutableListOf()
    var currentWishlist: WishlistsItem? = null
    var currentWishlistPosition: Int = 0
    var currentGift: GiftsItem? = null
    var currentGiftPosition: Int = 0
}