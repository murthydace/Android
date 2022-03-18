package com.local.growkart.dashboard.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.local.growkart.R
import com.local.growkart.ResourceState
import com.local.growkart.databinding.FragmentPersonalDetailBinding
import com.local.growkart.util.Error
import com.local.growkart.util.ErrorType
import com.local.growkart.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalDetailsFragment : Fragment() {
    lateinit var binding: FragmentPersonalDetailBinding
    private val viewModel: PersonalDetailViewModel by viewModels()
    val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_personal_detail, container, false)
        binding = DataBindingUtil.bind(root)!!
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initSignOut()
        observeError()
        observeStatus()
        return binding.root
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner, {
            it.error?.let { error -> ToastUtil.showErrorInfo(requireActivity(), error) }
        })
    }

    private fun observeStatus() {
        viewModel.status.observe(viewLifecycleOwner, {
            when (it) {
                Status.SUCCESS -> ToastUtil.showSuccessInfo(
                    requireActivity(),
                    "User Details updated successfully!"
                )
                else -> {
                    ToastUtil.showInfo(
                        requireContext(),
                        "Something went wrong. Try after sometime!"
                    )
                }
            }
        })
    }

    private fun initSignOut() {
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut {
                val action = PersonalDetailsFragmentDirections.actionProfileToLoginFragment()
                navController.safeNavigation(action)
            }
        }

    }

    fun NavController.safeNavigation(navDirections: NavDirections) {
        try {
            navController.navigate(navDirections)
        } catch (e: Exception) {

        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadPersonalDetail()
    }
}