package ru.otus.wishlist.fragment.wishlists.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentWishlistsInfoBinding

@AndroidEntryPoint
class WishlistsInfoFragment : Fragment(R.layout.fragment_wishlists_info) {

    private lateinit var binding: FragmentWishlistsInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistsInfoBinding.inflate(inflater)
        return binding.root
    }
}