package ru.otus.wishlist.fragment.wishlists.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentWishlistsEditBinding

@AndroidEntryPoint
class WishlistEditFragment : BottomSheetDialogFragment(R.layout.fragment_wishlists_edit) {

    private lateinit var binding: FragmentWishlistsEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistsEditBinding.inflate(inflater)
        return binding.root
    }
}