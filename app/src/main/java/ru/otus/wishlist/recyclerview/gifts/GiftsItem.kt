package ru.otus.wishlist.recyclerview.gifts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class GiftsItem(
    val id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Int = 0,
    private val mDeleteState: MutableLiveData<DeleteState> = MutableLiveData(DeleteState.NotSet)
) {

    val deleteState: LiveData<DeleteState>
        get() = mDeleteState

    fun clearDeleteState() {
        mDeleteState.value = DeleteState.NotSet
    }

    fun deleteInProgress() {
        mDeleteState.value = DeleteState.Loading
    }

    fun deleteSuccess() {
        mDeleteState.value = DeleteState.Success
    }

    fun deleteError() {
        mDeleteState.value = DeleteState.Error
    }

    sealed class DeleteState {

        data object NotSet: DeleteState()

        data object Loading: DeleteState()

        data object Success: DeleteState()

        data object Error : DeleteState()
    }
}
