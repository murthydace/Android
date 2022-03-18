package com.local.growkart.dashboard.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.local.growkart.GrowKartActivity
import com.local.growkart.GrowKartApp
import com.local.growkart.R
import com.local.growkart.ResourceState
import com.local.growkart.dashboard.adapter.ProductAdapter
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.dashboard.model.Product
import com.local.growkart.databinding.FragmentHomeBinding
import com.local.growkart.util.NotificationUtil
import com.local.growkart.util.ui.FirstLastSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var navController: NavController
    private val viewmodel: HomeViewModel by activityViewModels()


    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = DataBindingUtil.bind(view)!!
        binding.count = 0
        initProductList()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.init()
        setNavigation()
    }

    override fun onStart() {
        super.onStart()
        listenForProgressBar()
        listenForProductChange()
        listenForCartChanges()
    }

    private fun listenForProgressBar() {
        viewmodel.progressBarVisibility.observe(viewLifecycleOwner) {
            binding.showProgress = it
        }
    }

    private fun setNavigation() {
        if (!this::navController.isInitialized)
            navController = findNavController()
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.fab.setOnClickListener {
            NotificationUtil.sendCartReminderNotification(requireContext(), navController)
            navController.navigate(R.id.action_home_to_cart)
        }
    }

    private fun listenForCartChanges() {
        viewmodel.getCurrentCart().observe(viewLifecycleOwner, {
            Log.d(GrowKartActivity.TAG, "cart changed!!")
            binding.count = it.size
            adapter.updateCart(it)
        })
    }

    private fun listenForProductChange() {
        viewmodel.getAllProducts().observe(requireActivity(), {
            Log.d(GrowKartActivity.TAG, "product changed!! ${it.size.toString()}")
            viewmodel.hideProgress()
            adapter.updateProducts(it)
        })
    }

    private fun initProductList() {
        binding.productList.layoutManager = LinearLayoutManager(requireContext())
        binding.productList.addItemDecoration(
            FirstLastSpacingItemDecoration(
                resources.getDimensionPixelOffset(
                    R.dimen.dimen_16
                ), 0, FirstLastSpacingItemDecoration.Mode.VERTICAL
            )
        )
        adapter = ProductAdapter()
        adapter.listener = qtyChangeListener
        binding.productList.adapter = this.adapter
    }

    private val qtyChangeListener = object : ProductAdapter.QuantityChangeListener {
        override fun onQtyChange(product: Product, qty: Int) {
            Log.d(GrowKartActivity.TAG, "quantity changed ${product.name} and ${qty.toString()}")
            if (qty == 0)
                viewmodel.removeProductFromCart(product)
            else
                viewmodel.addProductToCart(CartModel(product, qty))
        }
    }


}