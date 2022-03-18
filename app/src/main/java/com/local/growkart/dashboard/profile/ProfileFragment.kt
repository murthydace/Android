package com.local.growkart.dashboard.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.local.growkart.GrowKartActivity
import com.local.growkart.GrowKartApp
import com.local.growkart.R
import com.local.growkart.dashboard.repository.CartRepository
import com.local.growkart.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
//    private val cart: CartRepository by lazy {
//        GrowKartApp.getInstance().cartRepository
//    }
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        binding = DataBindingUtil.bind(root)!!
        auth = FirebaseAuth.getInstance()
        navController = findNavController()//Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.btnSignOut.setOnClickListener {
//            cart.getCartFromRepo().removeObservers(viewLifecycleOwner)
//            cart.emptyCart()
            auth.signOut()
        }
        auth.addAuthStateListener {
            if (it.currentUser == null && navController.currentDestination?.id == R.id.profile) {//sign out
                navController.navigate(R.id.action_profile_to_loginFragment)
            }
        }
        return root
    }

}