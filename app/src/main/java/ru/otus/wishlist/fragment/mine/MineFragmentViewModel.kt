package ru.otus.wishlist.fragment.mine

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
import ru.otus.wishlist.recyclerview.wish.WishItem
import ru.otus.wishlist.recyclerview.wish.WishItemAdapter
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class MineFragmentViewModel @Inject constructor(
    private val useCase: MineFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mDataState = MutableLiveData<DataState>(DataState.NotSet)
    val dataState: LiveData<DataState>
        get() = mDataState
    private val mContentState = MutableLiveData<List<WishItem>>()
    val contentState: LiveData<List<WishItem>>
        get() = mContentState

    private var refreshDataTask: Job = Job()

    fun getAllWishlists() {
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
                            WishItem(
                                name = it.title.orEmpty(),
                                description = it.description.orEmpty()
                            )
                        }.toMutableList()
                        mContentState.value = cache.wishlists
                        mDataState.value = DataState.Content
                    }
            } catch (e: Throwable) {
                mDataState.value = DataState.Error
            }
        }
    }

    fun getOnScrollListener(adapter: WishItemAdapter, pageSize: Int) =
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

    fun saveCurrentWishlist(item: WishItem, position: Int) {
        cache.currentWishlist = item
        cache.currentWishlistPosition = position
    }

    sealed class DataState {

        data object NotSet: DataState()

        data object Loading: DataState()

        data object Empty: DataState()

        data object Content: DataState()

        data object Error : DataState()
    }
}