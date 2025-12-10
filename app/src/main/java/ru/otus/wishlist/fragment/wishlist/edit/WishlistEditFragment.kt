package ru.otus.wishlist.fragment.wishlist.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentWishlistEditBinding

@AndroidEntryPoint
class WishlistEditFragment : BottomSheetDialogFragment(R.layout.fragment_wishlist_edit) {

    private lateinit var binding: FragmentWishlistEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistEditBinding.inflate(inflater)
        return binding.root
    }
}