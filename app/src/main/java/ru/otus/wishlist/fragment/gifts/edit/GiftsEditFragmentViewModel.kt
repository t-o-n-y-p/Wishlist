package ru.otus.wishlist.fragment.gifts.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.otus.wishlist.R
import ru.otus.wishlist.WizardCache
import ru.otus.wishlist.api.models.Gift
import ru.otus.wishlist.databinding.FragmentGiftsEditBinding
import ru.otus.wishlist.fragment.FRAGMENT_GIFTS_CREATE
import ru.otus.wishlist.fragment.FRAGMENT_GIFTS_EDIT
import ru.otus.wishlist.recyclerview.gifts.GiftsItem
import javax.inject.Inject

@HiltViewModel
class GiftsEditFragmentViewModel @Inject constructor(
    private val useCase: GiftsEditFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mCreateOrEditState =
        MutableLiveData<CreateOrEditState>(CreateOrEditState.NotSet)
    val createOrEditState: LiveData<CreateOrEditState>
        get() = mCreateOrEditState

    fun fillFieldsFromCache(binding: FragmentGiftsEditBinding) =
        cache.currentGift?.apply {
            binding.nameInput.setText(name)
            binding.descriptionInput.setText(description)
            binding.priceInput.setText(price.toString())
        }

    fun createOrUpdateGift(name: String, description: String, price: Int) =
        viewModelScope.launch {
            try {
                cache.currentGift?.apply {
                    mCreateOrEditState.value = CreateOrEditState.Loading
                    useCase.updateGift(
                        id = id,
                        name = name,
                        description = description,
                        price = price
                    ).getOrThrow()
                    this.name = name
                    this.description = description
                    this.price = price
                    mCreateOrEditState.value = CreateOrEditState.Success
                } ?: let {
                    mCreateOrEditState.value = CreateOrEditState.Loading
                    val createResponse: Gift =
                        useCase.createGift(
                            wishlistId = cache.currentWishlist?.id.orEmpty(),
                            name = name,
                            description = description,
                            price = price
                        ).getOrThrow()
                    val newGift = GiftsItem(
                        id = createResponse.id.orEmpty(),
                        name = name,
                        description = description,
                        price = price
                    )
                    cache.currentWishlist?.gifts?.add(newGift)
                    mCreateOrEditState.value = CreateOrEditState.Success
                }
            } catch (_: Throwable) {
                mCreateOrEditState.value = CreateOrEditState.Error
            }
        }

    fun getToastText() =
        cache.currentGift?.let { R.string.gifts_updated } ?: R.string.gifts_created

    fun getFragmentResultRequestKey() =
        cache.currentGift?.let { FRAGMENT_GIFTS_EDIT } ?: FRAGMENT_GIFTS_CREATE

    sealed class CreateOrEditState {

        data object NotSet: CreateOrEditState()

        data object Loading: CreateOrEditState()

        data object Success: CreateOrEditState()

        data object Error : CreateOrEditState()
    }
}