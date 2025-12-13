package ru.otus.wishlist.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentAppMainBinding
import ru.otus.wishlist.fragment.FRAGMENT_PROFILE_TAB
import ru.otus.wishlist.fragment.LOGIN_STATUS
import ru.otus.wishlist.fragment.LOGOUT

@AndroidEntryPoint
class AppMainFragment : Fragment(R.layout.fragment_app_main) {

    private lateinit var binding: FragmentAppMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mine_menu -> navigate(R.id.go_to_mine_tab)
                R.id.users_menu -> navigate(R.id.go_to_users)
                R.id.profile_menu -> navigate(R.id.go_to_profile)
                else -> false
            }
        }
        binding.appMainFragmentContainer
            .getFragment<Fragment>()
            .childFragmentManager
            .setFragmentResultListener(
                FRAGMENT_PROFILE_TAB,
                viewLifecycleOwner
            ) { _, bundle ->
                when (bundle.getString(LOGIN_STATUS)) {
                    LOGOUT -> findNavController().navigate(R.id.logout)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this) {}
    }

    private fun navigate(resId: Int): Boolean {
        binding.appMainFragmentContainer.findNavController().navigate(resId)
        return true
    }
}