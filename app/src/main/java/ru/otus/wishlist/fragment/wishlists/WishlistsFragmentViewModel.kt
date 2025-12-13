package ru.otus.wishlist.fragment.wishlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.otus.wishlist.WizardCache
import ru.otus.wishlist.recyclerview.wishlists.WishlistsItem
import ru.otus.wishlist.recyclerview.wishlists.WishlistsItemAdapter
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class WishlistsFragmentViewModel @Inject constructor(
    private val useCase: WishlistsFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mDataState = MutableLiveData<DataState>(DataState.NotSet)
    val dataState: LiveData<DataState>
        get() = mDataState
    private val mContentState = MutableLiveData<List<WishlistsItem>>()
    val contentState: LiveData<List<WishlistsItem>>
        get() = mContentState

    private var refreshDataTask: Job = Job()

    fun fillWishlistsFromCache() {
        cache.wishlists
            .takeIf { it.isEmpty() }
            ?.let { loadWishlistsAndSaveToCache() }
            ?: let {
                mDataState.value = DataState.Loading
                mContentState.value = cache.wishlists
                mDataState.value = DataState.Content
            }
    }

    private fun loadWishlistsAndSaveToCache() {
        cache.wishlists.clear()
        refreshDataTask.cancel()
        refreshDataTask = viewModelScope.launch {
            try {
                mDataState.value = DataState.Loading
                val data = useCase.getAllWishlists().getOrThrow()
                data.takeIf { it.isEmpty() }
                    ?.let { mDataState.value = DataState.Empty }
                    ?: let {
                        cache.wishlists = data.map {
                            WishlistsItem(
                                id = it.id.orEmpty(),
                                title = it.title.orEmpty(),
                                description = it.description.orEmpty()
                            )
                        }.toMutableList()
                        mContentState.value = cache.wishlists
                        mDataState.value = DataState.Content
                    }
            } catch (_: Throwable) {
                mDataState.value = DataState.Error
            }
        }
    }

    fun getOnScrollListener(adapter: WishlistsItemAdapter, pageSize: Int) =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as? LinearLayoutManager)?.apply {
                    findLastVisibleItemPosition()
                        .takeIf { it == itemCount - 1 && itemCount < cache.wishlists.size }
                        ?.let {
                            adapter.submitList(
                                cache.wishlists.slice(
                                    0
                                            until
                                            min(cache.wishlists.size, itemCount + pageSize)
                                ))
                        }
                }
            }
        }

    fun saveCurrentWishlist(item: WishlistsItem, position: Int) {
        cache.currentWishlist = item
        cache.currentWishlistPosition = position
    }

    fun getCurrentWishlistPosition() = cache.currentWishlistPosition

    fun deleteWishlistItem(item: WishlistsItem) =
        viewModelScope.launch {
            try {
                item.deleteInProgress()
                useCase.deleteWishlist(item.id).getOrThrow()
                item.deleteSuccess()
                cache.wishlists.apply {
                    remove(item)
                    mContentState.value = this
                    ifEmpty { mDataState.value = DataState.Empty }
                }
            } catch (_: Throwable) {
                item.deleteError()
            }
        }

    fun clearCurrentWishlist() {
        cache.currentWishlist = null
        cache.currentWishlistPosition = 0
    }

    sealed class DataState {

        data object NotSet: DataState()

        data object Loading: DataState()

        data object Empty: DataState()

        data object Content: DataState()

        data object Error : DataState()
    }
}