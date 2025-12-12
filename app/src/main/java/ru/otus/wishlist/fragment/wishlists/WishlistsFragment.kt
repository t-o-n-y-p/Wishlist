package ru.otus.wishlist.fragment.wishlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentWishlistsBinding
import ru.otus.wishlist.fragment.getUniversalParcelable
import ru.otus.wishlist.fragment.wishlists.edit.WishlistEditFragment
import ru.otus.wishlist.recyclerview.wish.WishItemAdapter
import kotlin.math.min

@AndroidEntryPoint
class WishlistsFragment : Fragment(R.layout.fragment_wishlists) {

    private lateinit var binding: FragmentWishlistsBinding
    private val viewModel: WishlistsFragmentViewModel by viewModels()
    private val adapter: WishItemAdapter = WishItemAdapter(
        onEditButtonClicked = { item, position ->
            viewModel.saveCurrentWishlist(item, position)
            WishlistEditFragment().show(
                requireActivity().supportFragmentManager,
                WishlistEditFragment::class.simpleName)
        }
    )
    private val pageSize = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wishlistsContent.wishlists.apply {
            adapter = this@WishlistsFragment.adapter
            addOnScrollListener(viewModel.getOnScrollListener(this@WishlistsFragment.adapter, pageSize))
        }
        viewModel.dataState.observe(viewLifecycleOwner) {
            when (it) {
                WishlistsFragmentViewModel.DataState.NotSet -> {
                    binding.wishlistsLoading.loadingGroup.isVisible = false
                    binding.wishlistsContent.contentGroup.isVisible = false
                    binding.wishlistsError.errorGroup.isVisible = false
                    binding.wishlistsNoResults.noResultsGroup.isVisible = false
                }
                WishlistsFragmentViewModel.DataState.Loading -> {
                    binding.wishlistsLoading.loadingGroup.isVisible = true
                    binding.wishlistsContent.contentGroup.isVisible = false
                    binding.wishlistsError.errorGroup.isVisible = false
                    binding.wishlistsNoResults.noResultsGroup.isVisible = false
                }
                WishlistsFragmentViewModel.DataState.Content -> {
                    binding.wishlistsLoading.loadingGroup.isVisible = false
                    binding.wishlistsContent.contentGroup.isVisible = true
                    binding.wishlistsError.errorGroup.isVisible = false
                    binding.wishlistsNoResults.noResultsGroup.isVisible = false
                }
                WishlistsFragmentViewModel.DataState.Error -> {
                    binding.wishlistsLoading.loadingGroup.isVisible = false
                    binding.wishlistsContent.contentGroup.isVisible = false
                    binding.wishlistsError.errorGroup.isVisible = true
                    binding.wishlistsNoResults.noResultsGroup.isVisible = false
                }
                WishlistsFragmentViewModel.DataState.Empty -> {
                    binding.wishlistsLoading.loadingGroup.isVisible = false
                    binding.wishlistsContent.contentGroup.isVisible = false
                    binding.wishlistsError.errorGroup.isVisible = false
                    binding.wishlistsNoResults.noResultsGroup.isVisible = true
                }
            }
        }
        viewModel.contentState.observe(viewLifecycleOwner) {
            adapter.submitList(it.slice(0 until min(it.size, pageSize)))
        }
        binding.topAppBar.title =
            arguments?.getUniversalParcelable<WishlistsData>("data")
                ?.let { getString(R.string.wishlists_of_user).format(it.username) }
                ?: getString(R.string.mine)
        viewModel.getAllWishlists()
    }
}