package ru.otus.wishlist.recyclerview.wish

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.otus.wishlist.R

class WishItemViewHolder(
    view: View,
    private val onEditButtonClicked: (WishItem, Int) -> Unit = { _, _ -> }
) : RecyclerView.ViewHolder(view) {

    private val nameTextView: TextView by lazy {
        itemView.findViewById(R.id.title)
    }
    private val descriptionTextView: TextView by lazy {
        itemView.findViewById(R.id.subtitle)
    }
    private val editButton: Button by lazy {
        itemView.findViewById(R.id.edit_button)
    }

    fun bind(item: WishItem) {
        nameTextView.text = item.name
        descriptionTextView.text = item.description
        editButton.setOnClickListener {
            onEditButtonClicked(item, absoluteAdapterPosition)
        }
    }
}