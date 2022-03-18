package com.local.growkart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.local.growkart.databinding.ActivityGrowKartBinding
import com.local.growkart.login.other.Status
import com.local.growkart.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GrowKartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGrowKartBinding
    private lateinit var navController: NavController
    private val mainViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grow_kart)
        setupActionBar()
        setUpNavigation()
        listenForScreenChanges()

        mainViewModel.res.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data.let { res ->
                        if (res?.status == "success") {
                            res.data?.let { it1 ->
                                Log.d("data", "${it1[0]}")
                            }
                        } else {
                        }
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun setupActionBar() {
        val toolbar = binding.toolBar
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
        binding.toolBar.navigationIcon = null
    }

    private fun setUpNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(binding.bottomBarView.id)
        bottomNavigationView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onBackPressed() {
        val destination = navController.currentDestination
        if (destination != null && destination.id == R.id.home)//going back from dashboard shouldnt show Splash Screen or login
            finishAffinity()

        super.onBackPressed()
    }

    private fun listenForScreenChanges() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment,
                R.id.loginFragment,
                R.id.OTPFragment -> {
                    hideBottomNavBar()
                    supportActionBar?.hide()
                }
                R.id.home -> {
                    navController.graph.startDestination = R.id.home
                    showBottomNavBar()
                    supportActionBar?.show()
                    binding.toolBar.navigationIcon = null
                }
                else -> {
                    navController.graph.startDestination = R.id.home
                    showBottomNavBar()
                    supportActionBar?.show()
                    binding.toolBar.navigationIcon = null
                }
            }
        }
    }


    private fun hideBottomNavBar() {
        binding.bottomBarView.visibility = View.GONE
    }

    private fun showBottomNavBar() {
        binding.bottomBarView.visibility = View.VISIBLE
    }


    companion object {
        const val SPLASH_SCREEN_TIME_OUT: Long = 2000
        const val TAG = "GROWKARTTAG"
    }
}