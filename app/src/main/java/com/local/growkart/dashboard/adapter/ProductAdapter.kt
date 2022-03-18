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
import com.local.growkart.dashboard.cart.CartViewModel
import com.local.growkart.dashboard.model.Product
import com.local.growkart.databinding.ItemProductBinding
import com.local.growkart.util.StringUtil
import com.local.growkart.util.ui.QuantityCounterView
import com.squareup.picasso.Picasso

class ProductAdapter : BaseCallBackAdapter<ProductAdapter.ProductViewHolder>() {
    var products = listOf<Product>()
    var cart = listOf<CartModel>()
    lateinit var listener: QuantityChangeListener

    init {
        this.setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        Log.d(GrowKartActivity.TAG, "== adapter init")
        return ProductViewHolder(
            ItemProductBinding.inflate(inflater, parent, false),
            listener,
            parent.context
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products.get(position)
        holder.bind(product, getQtyForProductsInCart(product))
    }

    fun setCallBack(listener: QuantityChangeListener) {
        this.listener = listener
    }

    override fun getItemCount() = products.size

    fun updateCart(cart: List<CartModel>) {
        this.cart = cart.toList()
    }

    fun updateProducts(products: List<Product>) {
        this.products = products.toList()
        notifyDataSetChanged()
        Log.d(GrowKartActivity.TAG, "new list size == ${this.products.size}")
    }

    override fun getItemId(position: Int): Long {
        return products[position].id.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    private fun getQtyForProductsInCart(product: Product) =
        cart.find { it.product.id == product.id }?.qty ?: 0

    class ProductViewHolder(
        val binding: ItemProductBinding,
        val listener: QuantityChangeListener,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product, qty: Int) {
            binding.product = product
            if(!TextUtils.isEmpty(product.iconUrl))
                Picasso.get()
                    .load(product.iconUrl)
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
                        product,
                        qty
                    )

                }

                override fun onDecrement(qty: Int) {
                    Log.d(GrowKartActivity.TAG, "onDecrement")
                    listener.onQtyChange(product, qty)
                }
            }
            binding.qtyCounterView.binding.qty = qty.toString()
            binding.executePendingBindings()
        }

    }

    interface QuantityChangeListener {
        fun onQtyChange(product: Product, qty: Int)
    }
}

