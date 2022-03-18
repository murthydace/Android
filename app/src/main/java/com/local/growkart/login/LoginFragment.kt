package com.local.growkart.login

import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.local.growkart.GrowKartActivity
import com.local.growkart.R
import com.local.growkart.databinding.FragmentLoginBinding
import com.local.growkart.util.StringUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseLoginFragment() {
    lateinit var binding: FragmentLoginBinding
    private lateinit var verificationID: String
    private lateinit var refreshToken: PhoneAuthProvider.ForceResendingToken

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        binding = DataBindingUtil.bind(root)!!
        initViews()
        return root
    }

    private fun initViews() {
        binding.btnSubmit.setOnClickListener {
            validatePhoneNumber()
        }
        binding.btnGuestLogin.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_home)
        }
        binding.etPhoneNumber.addTextChangedListener(textWacher)
    }

    private val callBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            hideProgress()
            analytics.logEvent("Login_Success", Bundle().apply {
                "Log_in_time" to StringUtil.getCurrentTimeStamp()
            })
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            hideProgress()
            Log.d(GrowKartActivity.TAG, "failed")
            analytics.logEvent("Login_Failure", Bundle().apply {
                "reason" to p0.message
            })
            Toast.makeText(requireContext(), getString(R.string.try_again_error), Toast.LENGTH_LONG)
                .show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            verificationID = verificationId
            refreshToken = token
            Log.d(GrowKartActivity.TAG, "code sent")
            Toast.makeText(requireContext(), "Code sent!!", Toast.LENGTH_LONG).show()
            hideProgress()
            navigateToOTPVerification(verificationId)
        }

        override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
            super.onCodeAutoRetrievalTimeOut(verificationId)
            Log.d(GrowKartActivity.TAG, "timed out")
        }
    }
    private val textWacher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (char?.length ?: 0 >= 10) {
                binding.etPhoneNumber.clearFocus()
                hideKeyBoard()
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    private fun validatePhoneNumber() {
        showProgressBar()
        hideKeyBoard()
        val phoneNumber = binding.etPhoneNumber.text.toString()
        if (validPhoneNumber(phoneNumber)) {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91$phoneNumber") // Phone number to verify, appending +91 assuming indian phone numbers
                .setTimeout(0L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity()) // Activity (for callback binding)
                .setCallbacks(callBacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } else {
            hideProgress()
            binding.etPhoneNumber.error = getString(R.string.invalid_phone_no)
            Toast.makeText(requireContext(), "Phone Number is invalid", Toast.LENGTH_LONG).show()
        }
    }

    private fun validPhoneNumber(phoneNumber: String): Boolean { // Move this to util if called in multiple places
        if (TextUtils.isEmpty(phoneNumber)) return false
        val phoneNumberRegex = Regex("^[6-9]{1}[0-9]{9}$")
        return phoneNumber.matches(phoneNumberRegex)
    }

    private fun showProgressBar() {
        binding.pgBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.pgBar.visibility = View.GONE
    }


}
