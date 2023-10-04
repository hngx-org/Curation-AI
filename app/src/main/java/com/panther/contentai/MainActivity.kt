package com.panther.contentai

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.panther.contentai.arch_comp.CuratorViewModel
import com.panther.contentai.databinding.ActivityMainBinding
import com.panther.contentai.fragments.SubscriptionDirections
import com.panther.contentai.util.Resource
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val curatorViewModel by viewModels<CuratorViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = CuratorSharedPreference.initSharedPref(this)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment

        val navController = navHostFragment.navController
        val drawerLayout: DrawerLayout = binding.drawerLayout

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.chat_dest), drawerLayout
        )

        val firstLaunch = CuratorSharedPreference().fetchSharedPref()
        if (!firstLaunch){
            navController.navigate(R.id.signinScreen)
        }

        navController.addOnDestinationChangedListener{_,destination,_->
            binding.toolbar.isVisible = destination.id == R.id.chat_dest
            if (appBarConfiguration.topLevelDestinations.contains(destination.id)){
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }else{
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        setupDrawerLayout(navController)

        binding.plusSubscriptionBtn.setOnClickListener {
            val route = SubscriptionDirections.actionGlobalSubscription()
            navController.navigate(route)
        }

        lifecycleScope.launch {
            curatorViewModel.userDataState.collect { state ->
                if (state is Resource.Successful) {
                    try {
                        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.name_tv).text = state.data?.name
                        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.email_tv).text = state.data?.email
                    }catch (e:Exception){
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }


    }

    private fun setupDrawerLayout(navController: NavController) {
        val drawerLayout = findViewById<NavigationView>(R.id.navigationView)
        drawerLayout.setupWithNavController(navController)
    }
}