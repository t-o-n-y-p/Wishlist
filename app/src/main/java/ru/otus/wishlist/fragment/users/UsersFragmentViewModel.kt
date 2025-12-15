package ru.otus.wishlist.fragment.users

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
import ru.otus.wishlist.recyclerview.users.UsersItem
import ru.otus.wishlist.recyclerview.users.UsersItemAdapter
import ru.otus.wishlist.recyclerview.wishlists.WishlistsItem
import javax.inject.Inject
import kotlin.collections.toMutableList
import kotlin.math.min

@HiltViewModel
class UsersFragmentViewModel @Inject constructor(
    private val useCase: UsersFragmentUseCase,
    private val cache: WizardCache
) : ViewModel() {

    private val mDataState = MutableLiveData<DataState>(DataState.NotSet)
    val dataState: LiveData<DataState>
        get() = mDataState
    private val mContentState = MutableLiveData<List<UsersItem>>()
    val contentState: LiveData<List<UsersItem>>
        get() = mContentState

    private var refreshDataTask: Job = Job()

    fun fillUsersFromCache() {
        cache.users
            .takeIf { it.isEmpty() }
            ?.let { loadUsersAndSaveToCache() }
            ?: let {
                mDataState.value = DataState.Loading
                mContentState.value = cache.users
                mDataState.value = DataState.Content
            }
    }

    fun loadUsersAndSaveToCache() {
        cache.wishlists.clear()
        refreshDataTask.cancel()
        refreshDataTask = viewModelScope.launch {
            try {
                mDataState.value = DataState.Loading
                val data = useCase.getAllUsers().getOrThrow()
                data.takeIf { it.isEmpty() }
                    ?.let { mDataState.value = DataState.Empty }
                    ?: let {
                        cache.users = data.map {
                            UsersItem(
                                id = it.id.orEmpty(),
                                username = it.username.orEmpty()
                            )
                        }.toMutableList()
                        mContentState.value = cache.users
                        mDataState.value = DataState.Content
                    }
            } catch (_: Throwable) {
                mDataState.value = DataState.Error
            }
        }
    }

    fun getOnScrollListener(adapter: UsersItemAdapter, pageSize: Int) =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as? LinearLayoutManager)?.apply {
                    findLastVisibleItemPosition()
                        .takeIf { it == itemCount - 1 && itemCount < cache.users.size }
                        ?.let {
                            adapter.submitList(
                                cache.users.slice(
                                    0
                                            until
                                            min(cache.users.size, itemCount + pageSize)
                                ))
                        }
                }
            }
        }

    fun saveCurrentUser(item: UsersItem) {
        cache.currentUser = item
    }

    sealed class DataState {

        data object NotSet: DataState()

        data object Loading: DataState()

        data object Empty: DataState()

        data object Content: DataState()

        data object Error : DataState()
    }
}