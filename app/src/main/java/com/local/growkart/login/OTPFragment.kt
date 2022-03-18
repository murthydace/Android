package com.local.growkart.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.PhoneAuthProvider
import com.local.growkart.R
import com.local.growkart.databinding.FragmentOtpVerificationBinding

class OTPFragment : BaseLoginFragment(), SmsReciever.SmsListener {
    private lateinit var binding: FragmentOtpVerificationBinding
    val args: OTPFragmentArgs by navArgs()
    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SmsReciever? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_otp_verification, container, false)
        binding = DataBindingUtil.bind(root)!!
        return root
    }

    override fun onStart() {
        super.onStart()
        listenForOTPSms()
        requireActivity().registerReceiver(smsReceiver, intentFilter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.btnVerify.setOnClickListener {
            showProgressBar()
            val otp = binding.etOtpVerification.text.toString()
            if (TextUtils.isEmpty(otp))
                Toast.makeText(requireContext(), "Please enter a valid OTP", Toast.LENGTH_SHORT)
                    .show()
            else {
                val credential = PhoneAuthProvider.getCredential(args.verificationId, otp)
                siginWithCredential(credential)
            }
        }
        binding.etOtpVerification.addTextChangedListener(textWacher)
    }

    private fun showProgressBar() {
        binding.pgBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {

        binding.pgBar.visibility = View.GONE
    }

    private fun listenForOTPSms() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = SmsReciever()
        smsReceiver?.updateListener(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        try {
            requireActivity().unregisterReceiver(smsReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOTPRecieved(otp: String) {
        binding.etOtpVerification.setText(otp)
        requireActivity().unregisterReceiver(smsReceiver)
    }

    val textWacher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (char?.length ?: 0 >= 6) {
                binding.etOtpVerification.clearFocus()
                hideKeyBoard()
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

}