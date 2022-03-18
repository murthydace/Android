package com.local.growkart.order.model

import android.os.Parcelable
import androidx.databinding.BindingAdapter
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.util.StringUtil
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Order(
    var orderId: String = "",
    val customerId: String = "",
    var status: OrderStatus = OrderStatus.Ordered,
    val createdDate: Long = StringUtil.getCurrentTimeStamp(),
    val billAmount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val dueAmount: Double = 0.0,
    val discount: Double = 0.0,
    val refund: Double = 0.0,
    val details: String = "",
    val completed: Boolean = false,
    val icon: String? = "",
    val items: List<OrderItem> = emptyList()
) : Parcelable {
    constructor() : this("")

    fun getGrandTotal(): Float = billAmount.toFloat()
    fun getProductNames() = items.joinToString { it.itemName }
    fun getExtraProductCount() = items.size.minus(1)
}