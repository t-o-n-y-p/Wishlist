package ru.otus.wishlist

import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.otus.wishlist.databinding.ActivityMainBinding
import ru.otus.wishlist.storage.UserPreferences
import ru.otus.wishlist.storage.get
import ru.otus.wishlist.storage.put
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (sharedPreferences.get<UserPreferences>()?.isLoggedIn() ?: false) {
            binding.fragmentContainer
                .getFragment<NavHostFragment>()
                .navController
                .navigate(R.id.go_to_app_main)
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBar = insets.getInsets(
                WindowInsets.Type.statusBars()
                        or WindowInsets.Type.navigationBars())
            v.updatePadding(
                left = systemBar.left,
                top = systemBar.top,
                right = systemBar.right,
                bottom = systemBar.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
        setContentView(binding.root)
    }
}