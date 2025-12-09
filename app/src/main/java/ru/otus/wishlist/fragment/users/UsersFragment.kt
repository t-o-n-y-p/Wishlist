package ru.otus.wishlist.fragment.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentUsersBinding

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_users) {

    private lateinit var binding: FragmentUsersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater)
        return binding.root
    }
}