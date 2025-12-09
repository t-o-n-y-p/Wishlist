package ru.otus.wishlist.fragment.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.otus.wishlist.databinding.FragmentProfileBinding
import ru.otus.wishlist.storage.UserPreferences
import ru.otus.wishlist.storage.cleanup
import ru.otus.wishlist.storage.get
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    fun fillDataFromPreferences(binding: FragmentProfileBinding) =
        sharedPreferences.get<UserPreferences>()
            ?.let { binding.usernameText.text = it.name }

    fun logout() = sharedPreferences.cleanup<UserPreferences>()
}