package com.local.growkart.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.local.growkart.R
import com.local.growkart.databinding.FragmentOrderDetailsBinding
import com.local.growkart.order.model.Order
import com.local.growkart.util.StringUtil
import com.local.growkart.util.ui.FirstLastSpacingItemDecoration

class OrderDetailFragment : Fragment() {
    val args: OrderDetailFragmentArgs by navArgs()
    lateinit var orderDetails: Order
    lateinit var binding: FragmentOrderDetailsBinding
    lateinit var adapter: OrderDetailAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_order_details, container, false)
        binding = DataBindingUtil.bind(root)!!
        initList()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderDetails = args.orderDetails
        binding.order = orderDetails
        binding.executePendingBindings()
        binding.tvOrderCountValue.text = orderDetails.items.size.toString()
        binding.tvOrderTotalValue.text =
            "${StringUtil.getCurrencySymbol()} ${orderDetails.getGrandTotal()}"
        adapter.updateOrderList(orderDetails.items)
        initEditOrder()
    }

    private fun initList() {
        binding.orderList.layoutManager = LinearLayoutManager(requireContext())
        binding.orderList.addItemDecoration(
            FirstLastSpacingItemDecoration(
                resources.getDimensionPixelOffset(
                    R.dimen.dimen_16
                ), 0, FirstLastSpacingItemDecoration.Mode.VERTICAL
            )
        )
        adapter = OrderDetailAdapter()
        binding.orderList.adapter = this.adapter
    }

    fun initEditOrder() {
        binding.ivEditOrder.setOnClickListener {
            val direction = OrderDetailFragmentDirections.actionOrderDetailToEditOrderFragment(
                orderDetails
            )
            findNavController().navigate(direction)
        }

    }
}