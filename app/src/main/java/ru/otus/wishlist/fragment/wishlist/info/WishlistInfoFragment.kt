package ru.otus.wishlist.fragment.wishlist.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentWishlistInfoBinding

@AndroidEntryPoint
class WishlistInfoFragment : Fragment(R.layout.fragment_wishlist_info) {

    private lateinit var binding: FragmentWishlistInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistInfoBinding.inflate(inflater)
        return binding.root
    }
}