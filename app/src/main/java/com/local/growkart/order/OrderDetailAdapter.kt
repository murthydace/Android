package com.local.growkart.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.local.growkart.R
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.databinding.ItemOrderBinding
import com.local.growkart.databinding.ItemProductBinding
import com.local.growkart.order.model.OrderItem

class OrderDetailAdapter : RecyclerView.Adapter<OrderDetailAdapter.OrderViewHolder>() {

    private lateinit var orderItems: List<OrderItem>

    fun updateOrderList(items: List<OrderItem>) {
        this.orderItems = items.toList()
        notifyDataSetChanged()
    }

    class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: OrderItem) {
            binding.cartModel = model
            binding.tvQty.text = model.quantity.toString()
            binding.tvPrice.text = model.totalPrice.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderItems[position])
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }


}