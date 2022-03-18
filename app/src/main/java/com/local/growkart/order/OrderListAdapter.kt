package com.local.growkart.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.local.growkart.databinding.ItemOrderListBinding
import com.local.growkart.order.model.Order

class OrderListAdapter(val orderSelectedListener: (order: Order) -> Unit) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    val orders = mutableListOf<Order>()

    inner class ViewHolder(val binding: ItemOrderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.order = order
            binding.root.setOnClickListener {
                orderSelectedListener(order)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemOrderListBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount() = orders.size

    fun updateOrders(orders: List<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
        this.orders.sortByDescending { it.createdDate }
        notifyDataSetChanged()
    }

}