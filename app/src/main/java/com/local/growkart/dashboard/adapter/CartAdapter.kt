package com.local.growkart.dashboard.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.local.growkart.BaseCallBackAdapter
import com.local.growkart.GrowKartActivity
import com.local.growkart.R
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.dashboard.model.Product
import com.local.growkart.databinding.ItemCartBinding
import com.local.growkart.util.ui.QuantityCounterView
import com.squareup.picasso.Picasso

class CartAdapter : BaseCallBackAdapter<CartAdapter.CartViewHolder>() {
    var cart = listOf<CartModel>()
    lateinit var listener: CartChangeListener

    init {
        this.setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        Log.d(GrowKartActivity.TAG, "== adapter init")
        return CartViewHolder(
            ItemCartBinding.inflate(inflater, parent, false),
            listener,
            parent.context
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cart.get(position))
    }

    override fun getItemCount() = cart.size

    fun updateProducts(cart: List<CartModel>) {
        this.cart = cart.toList()
        notifyDataSetChanged()
        Log.d(GrowKartActivity.TAG, "new list size == ${this.cart.size}")
    }

    override fun getItemId(position: Int): Long {
        return cart[position].product.id.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    class CartViewHolder(
        val binding: ItemCartBinding,
        val listener: CartChangeListener,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartModel: CartModel) {
            binding.cartModel = cartModel
            if (!TextUtils.isEmpty(cartModel.product.iconUrl))
                Picasso.get()
                    .load(cartModel.product.iconUrl)
                    .placeholder(R.drawable.ic_cart)
                    .fit()
                    .centerCrop()
                    .into(binding.imProductIcon)
            else
                binding.imProductIcon.setImageResource(R.drawable.ic_cart)
            binding.qtyCounterView.initView(context)
            binding.qtyCounterView.listener = object : QuantityCounterView.onChangeListener {
                override fun onIncrement(qty: Int) {
                    Log.d(GrowKartActivity.TAG, "onincrement")
                    listener.onQtyChange(
                        cartModel.product,
                        qty
                    )
                }

                override fun onDecrement(qty: Int) {
                    Log.d(GrowKartActivity.TAG, "onDecrement")
                    listener.onQtyChange(cartModel.product, qty)
                }
            }
            binding.imDelete.setOnClickListener {
                listener.onDelete(cartModel.product)
            }
            binding.qtyCounterView.binding.qty = cartModel.qty.toString()
            binding.executePendingBindings()
        }

    }

    interface CartChangeListener {
        fun onQtyChange(product: Product, qty: Int)
        fun onDelete(product: Product)
    }
}

