package ru.otus.wishlist.recyclerview.wish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.otus.wishlist.R

class WishItemAdapter(
    private val onEditButtonClicked: (WishItem, Int) -> Unit
) : ListAdapter<WishItem, WishItemViewHolder>(WishItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WishItemViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_wishlist_info, parent, false),
            onEditButtonClicked = onEditButtonClicked
        )

    override fun onBindViewHolder(holder: WishItemViewHolder, position: Int) =
        holder.bind(getItem(position))
}