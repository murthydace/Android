package com.local.growkart.util.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.local.growkart.GrowKartActivity
import com.local.growkart.databinding.ViewIncrementDecrementButtonBinding

class QuantityCounterView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    var binding =
        ViewIncrementDecrementButtonBinding.inflate(LayoutInflater.from(context), this, true)
    var qty: Int = 0
        get() {
            return if (binding.tvQty.text.isNotEmpty()) {
                Integer.parseInt(
                    binding.tvQty.text.toString()
                )
            } else {
                0
            }
        }

    lateinit var listener: onChangeListener

    fun initView(context: Context) {
        binding.qty = "0"

        binding.imIncrement.setOnClickListener { _ ->
            binding.qty = qty.plus(1).toString()
            Log.d(GrowKartActivity.TAG, "custom onincrement")
            if (this::listener.isInitialized) {
                Log.d(GrowKartActivity.TAG, "custom onincrement condition")
                listener.onIncrement(qty.plus(1))
            }
        }
        binding.imDecrement.setOnClickListener { _ ->
            if (qty > 0)
                binding.qty = qty.minus(1).toString()
            else
                Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show()
            if (this::listener.isInitialized && qty > 0)
                listener.onDecrement(qty.minus(1))
        }
    }

    fun setQuantity(qty: Int) {
        binding.tvQty.text = qty.toString()
    }

    interface onChangeListener {
        fun onIncrement(qty: Int)
        fun onDecrement(qty: Int)
    }
}