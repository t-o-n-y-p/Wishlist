package ru.otus.wishlist

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ru.otus.wishlist.recyclerview.wishlists.WishlistsItem
import javax.inject.Inject

@ActivityRetainedScoped
class WizardCache @Inject constructor() {

    var wishlists: MutableList<WishlistsItem> = mutableListOf()
    var currentWishlist: WishlistsItem? = null
    var currentWishlistPosition: Int = 0
}