package ru.otus.wishlist.recyclerview.wish

import androidx.recyclerview.widget.DiffUtil

class WishItemCallback : DiffUtil.ItemCallback<WishItem>() {

    override fun areItemsTheSame(oldItem: WishItem, newItem: WishItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: WishItem, newItem: WishItem): Boolean =
        oldItem.name == newItem.name
}