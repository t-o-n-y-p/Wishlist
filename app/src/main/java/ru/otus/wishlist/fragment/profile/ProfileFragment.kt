package ru.otus.wishlist.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentProfileBinding
import ru.otus.wishlist.fragment.FRAGMENT_PROFILE
import ru.otus.wishlist.fragment.LOGIN_STATUS
import ru.otus.wishlist.fragment.LOGOUT
import ru.otus.wishlist.fragment.setFragmentResult
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logOutButton.setOnClickListener {
            viewModel.logout()
            setFragmentResult(
                fragment = FRAGMENT_PROFILE,
                key = LOGIN_STATUS,
                value = LOGOUT
            )
        }
        viewModel.fillDataFromPreferences(binding)
    }
}