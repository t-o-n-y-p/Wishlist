package ru.otus.wishlist.fragment.gifts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentGiftsBinding
import ru.otus.wishlist.fragment.FRAGMENT_GIFTS_CREATE
import ru.otus.wishlist.fragment.FRAGMENT_GIFTS_EDIT
import ru.otus.wishlist.fragment.RESULT
import ru.otus.wishlist.fragment.SUCCESS
import ru.otus.wishlist.fragment.gifts.edit.GiftsEditFragment
import ru.otus.wishlist.fragment.showConfirmationAlert
import ru.otus.wishlist.fragment.showErrorAlert
import ru.otus.wishlist.recyclerview.gifts.GiftsItem
import ru.otus.wishlist.recyclerview.gifts.GiftsItemAdapter
import kotlin.math.min

@AndroidEntryPoint
class GiftsFragment : Fragment(R.layout.fragment_gifts) {

    private lateinit var binding: FragmentGiftsBinding
    private val viewModel: GiftsFragmentViewModel by viewModels()
    private val adapter: GiftsItemAdapter = GiftsItemAdapter(
        onEditButtonClicked = { item, position ->
            viewModel.saveCurrentGift(item, position)
            item.clearDeleteState()
            GiftsEditFragment().show(
                parentFragmentManager,
                GiftsEditFragment::class.simpleName)
        },
        onDeleteButtonClicked = { item ->
            requireActivity().showConfirmationAlert { _, _ ->
                viewModel.deleteGiftItem(item)
            }
        },
        onBind = { item, actionGroup, loadingGroup ->
            item.deleteState.observe(viewLifecycleOwner) {
                when (it) {
                    GiftsItem.DeleteState.NotSet -> {
                        actionGroup.isVisible = true
                        loadingGroup.isVisible = false
                    }
                    GiftsItem.DeleteState.Loading -> {
                        actionGroup.isVisible = false
                        loadingGroup.isVisible = true
                    }
                    GiftsItem.DeleteState.Success -> {
                        Toast.makeText(
                            context,
                            R.string.gifts_deleted,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    GiftsItem.DeleteState.Error -> {
                        actionGroup.isVisible = true
                        loadingGroup.isVisible = false
                        requireContext().showErrorAlert()
                    }
                }
            }
        }
    )
    private val pageSize = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.giftsContent.gifts.apply {
            adapter = this@GiftsFragment.adapter
            addOnScrollListener(viewModel.getOnScrollListener(this@GiftsFragment.adapter, pageSize))
        }
        setFragmentResultListener(FRAGMENT_GIFTS_EDIT) { _, bundle ->
            when (bundle.getString(RESULT)) {
                SUCCESS -> adapter.notifyItemChanged(viewModel.getCurrentGiftPosition())
            }
        }
        setFragmentResultListener(FRAGMENT_GIFTS_CREATE) { _, bundle ->
            when (bundle.getString(RESULT)) {
                SUCCESS -> viewModel.fillGiftsFromCache()
            }
        }
        viewModel.dataState.observe(viewLifecycleOwner) {
            when (it) {
                GiftsFragmentViewModel.DataState.NotSet -> {
                    binding.giftsLoading.loadingGroup.isVisible = false
                    binding.giftsContent.contentGroup.isVisible = false
                    binding.giftsError.errorGroup.isVisible = false
                    binding.giftsNoResults.noResultsGroup.isVisible = false
                }
                GiftsFragmentViewModel.DataState.Loading -> {
                    binding.giftsLoading.loadingGroup.isVisible = true
                    binding.giftsContent.contentGroup.isVisible = false
                    binding.giftsError.errorGroup.isVisible = false
                    binding.giftsNoResults.noResultsGroup.isVisible = false
                }
                GiftsFragmentViewModel.DataState.Content -> {
                    binding.giftsLoading.loadingGroup.isVisible = false
                    binding.giftsContent.contentGroup.isVisible = true
                    binding.giftsError.errorGroup.isVisible = false
                    binding.giftsNoResults.noResultsGroup.isVisible = false
                }
                GiftsFragmentViewModel.DataState.Error -> {
                    binding.giftsLoading.loadingGroup.isVisible = false
                    binding.giftsContent.contentGroup.isVisible = false
                    binding.giftsError.errorGroup.isVisible = true
                    binding.giftsNoResults.noResultsGroup.isVisible = false
                }
                GiftsFragmentViewModel.DataState.Empty -> {
                    binding.giftsLoading.loadingGroup.isVisible = false
                    binding.giftsContent.contentGroup.isVisible = false
                    binding.giftsError.errorGroup.isVisible = false
                    binding.giftsNoResults.noResultsGroup.isVisible = true
                }
            }
        }
        viewModel.contentState.observe(viewLifecycleOwner) {
            adapter.submitList(it.slice(0 until min(it.size, pageSize)))
        }
        binding.addButton.setOnClickListener {
            viewModel.clearCurrentGift()
            GiftsEditFragment().show(
                parentFragmentManager,
                GiftsEditFragment::class.simpleName)
        }
        binding.topAppBar.title =
            viewModel.getCurrentWishlist()?.let { wishlist ->
                viewModel.getCurrentUser()?.let { user ->
                    binding.addButton.isVisible = false
                    getString(R.string.wishlist_of_user_title)
                        .format(user.username, wishlist.title)
                } ?: wishlist.title
            }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.go_to_wishlists)
        }
        viewModel.fillGiftsFromCache()
    }
}