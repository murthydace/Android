package com.local.growkart.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.local.growkart.R
import com.local.growkart.ResourceState
import com.local.growkart.databinding.FragmentOrderListBinding
import com.local.growkart.order.viewmodel.OrderViewModel
import com.local.growkart.util.ToastUtil
import com.local.growkart.util.ui.FirstLastSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderListFragment : Fragment() {
    lateinit var binding: FragmentOrderListBinding
    lateinit var navController: NavController
    lateinit var orderListAdapter: OrderListAdapter
    val viewModel: OrderViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_order_list, container, false)
        binding = DataBindingUtil.bind(root)!!
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner

        }
        initNavigation()
        initOrderList()
        loadOrders()
        return root
    }

    private fun loadOrders() {
        viewModel.getOrders().observe(viewLifecycleOwner) {
            if (it != null)
                when (it.state) {
                    ResourceState.LOADING -> {
                        viewModel.showProgress.value = true
                        viewModel.showOrders.value = false
                    }
                    ResourceState.SUCCESS -> {
                        viewModel.showProgress.value = false
                        viewModel.showOrders.value = true
                        orderListAdapter.updateOrders(it.data ?: emptyList())
                    }
                    ResourceState.ERROR -> {
                        viewModel.showProgress.value = false
                        ToastUtil.showErrorInfo(
                            requireActivity(),
                            it.message ?: getString(R.string.try_again_error)
                        )
                    }
                }
        }
    }

    private fun initNavigation() {
        if (!this::navController.isInitialized)
            navController = findNavController()//Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    private fun initOrderList() {
        orderListAdapter = OrderListAdapter {
            val action = OrderListFragmentDirections.actionOrderToOrderDetail(it)
            navController.navigate(action)
        }

        binding.orderList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderListAdapter
            addItemDecoration(
                FirstLastSpacingItemDecoration(
                    resources.getDimensionPixelOffset(
                        R.dimen.dimen_16
                    ), 0, FirstLastSpacingItemDecoration.Mode.VERTICAL
                )
            )
        }
    }
}