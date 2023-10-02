package com.panther.contentai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.panther.contentai.databinding.ActivityMainBinding
import com.panther.contentai.fragments.SubscriptionDirections


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }

    private fun setupDrawerLayout(navController: NavController) {
        val drawerLayout = findViewById<NavigationView>(R.id.navigationView)
        drawerLayout.setupWithNavController(navController)
    }
}