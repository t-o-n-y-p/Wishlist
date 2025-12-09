package ru.otus.wishlist.fragment.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.R
import ru.otus.wishlist.databinding.FragmentMineBinding

@AndroidEntryPoint
class MineFragment : Fragment(R.layout.fragment_mine) {

    private lateinit var binding: FragmentMineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMineBinding.inflate(inflater)
        return binding.root
    }
}