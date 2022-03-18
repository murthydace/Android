package com.local.growkart.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.local.growkart.GrowKartActivity
import com.local.growkart.R

class SplashFragment : Fragment() {
    private lateinit var navcontroller: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onResume() {
        super.onResume()
        navcontroller = findNavController()//Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        dismissSplashOnTimeOut()
    }

    fun dismissSplashOnTimeOut() {
        Handler().postDelayed({
            navcontroller.navigate(R.id.action_to_login)
        }, GrowKartActivity.SPLASH_SCREEN_TIME_OUT)
    }
}