package ru.otus.wishlist

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ru.otus.wishlist.recyclerview.wish.WishItem
import javax.inject.Inject

@ActivityRetainedScoped
class WizardCache @Inject constructor() {

    var wishlists: MutableList<WishItem> = mutableListOf()
    var currentWishlist: WishItem = WishItem()
    var currentWishlistPosition: Int = 0
}