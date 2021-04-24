package ru.loyalman.android.aviasalesloginov

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.loyalman.android.aviasalesloginov.base.BaseActivity
import ru.loyalman.android.aviasalesloginov.base.BaseNavigation
import ru.loyalman.android.aviasalesloginov.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var navigation: BaseNavigation

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        navigation.bindController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigation.unbindController()
    }

    override fun onNavigateUp(): Boolean = navController.navigateUp()
}