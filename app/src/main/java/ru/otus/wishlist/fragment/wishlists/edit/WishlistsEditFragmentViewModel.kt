package ru.otus.wishlist.fragment.wishlists.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.otus.wishlist.R
import ru.otus.wishlist.WizardCache
import ru.otus.wishlist.api.models.Wishlist
import ru.otus.wishlist.databinding.FragmentWishlistsEditBinding
import ru.otus.wishlist.fragment.FRAGMENT_WISHLISTS_CREATE
import ru.otus.wishlist.fragment.FRAGMENT_WISHLISTS_EDIT
import ru.otus.wishlist.recyclerview.wishlists.WishlistsItem
import javax.inject.Inject

@HiltViewModel
class WishlistsEditFragmentViewModel @Inject constructor(
    private val useCase: WishlistsEditFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mCreateOrEditState = MutableLiveData<CreateOrEditState>(CreateOrEditState.NotSet)
    val createOrEditState: LiveData<CreateOrEditState>
        get() = mCreateOrEditState

    fun fillFieldsFromCache(binding: FragmentWishlistsEditBinding) =
        cache.currentWishlist?.apply {
            binding.wishlistNameInput.setText(title)
            binding.wishlistDescriptionInput.setText(description)
        }

    fun createOrUpdateWishlist(title: String, description: String) =
        viewModelScope.launch {
            try {
                cache.currentWishlist?.apply {
                    mCreateOrEditState.value = CreateOrEditState.Loading
                    useCase.updateWishlist(
                        id = id,
                        title = title,
                        description = description
                    ).getOrThrow()
                    this.title = title
                    this.description = description
                    mCreateOrEditState.value = CreateOrEditState.Success
                } ?: let {
                    mCreateOrEditState.value = CreateOrEditState.Loading
                    val createResponse: Wishlist =
                        useCase.createWishlist(
                            title = title,
                            description = description
                        ).getOrThrow()
                    val newWishlist = WishlistsItem(
                        id = createResponse.id.orEmpty(),
                        title = createResponse.title.orEmpty(),
                        description = createResponse.description.orEmpty()
                    )
                    cache.wishlists.add(newWishlist)
                    mCreateOrEditState.value = CreateOrEditState.Success
                }
            } catch (_: Throwable) {
                mCreateOrEditState.value = CreateOrEditState.Error
            }
        }

    fun getToastText() =
        cache.currentWishlist?.let { R.string.wishlists_updated } ?: R.string.wishlists_created

    fun getFragmentResultRequestKey() =
        cache.currentWishlist?.let { FRAGMENT_WISHLISTS_EDIT } ?: FRAGMENT_WISHLISTS_CREATE

    sealed class CreateOrEditState {

        data object NotSet: CreateOrEditState()

        data object Loading: CreateOrEditState()

        data object Success: CreateOrEditState()

        data object Error : CreateOrEditState()
    }
}