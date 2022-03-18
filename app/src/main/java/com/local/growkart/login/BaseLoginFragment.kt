package com.local.growkart.login

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.local.growkart.GrowKartApp
import com.local.growkart.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseLoginFragment : Fragment() {
    @Inject
    lateinit var auth: FirebaseAuth
    lateinit var navController: NavController
    var currentUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()//Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    abstract fun hideProgress()
    override fun onStart() {
        super.onStart()
        currentUser = auth.currentUser
        if (currentUser != null)
            navController.navigate(R.id.action_loginFragment_to_home)
    }

    protected fun siginWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                hideProgress()
                currentUser = auth.currentUser // this user needs to be saved in Firestore
                navigateToHomeOnOTPVerifed()
            }
            .addOnFailureListener {
                hideProgress()
            }
    }


    fun navigateToOTPVerification(verificationId: String) {
        navController.navigate(
            LoginFragmentDirections.actionLoginFragmentToOTPFragment(
                verificationId
            )
        )
    }

    protected fun hideKeyBoard() {
        val manager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun navigateToHomeOnOTPVerifed() =
        navController.navigate(R.id.action_OTPFragment_to_home)
}