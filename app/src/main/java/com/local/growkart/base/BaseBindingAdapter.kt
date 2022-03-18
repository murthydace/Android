package com.local.growkart

import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.local.growkart.dashboard.model.User
import com.local.growkart.order.model.Order
import com.local.growkart.order.model.OrderStatus
import com.local.growkart.util.StringUtil
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(iv: ImageView, iconUrl: String) {
    if (!TextUtils.isEmpty(iconUrl))
        Picasso.get()
            .load(iconUrl)
            .placeholder(R.drawable.ic_cart)
            .fit()
            .centerCrop()
            .into(iv)
    else
        iv.setImageResource(R.drawable.ic_cart)
}

@BindingAdapter("orderDate")
fun setOrderDate(tv: TextView, order: Order?) {
    try {
        tv.text = StringUtil.getDateFromEpochTimeStamp(order?.createdDate)
    } catch (e: Exception) {
        tv.text = ""
    }
}

@BindingAdapter("statusColor")
fun setTextColorBasedOnOrderStatus(tv: TextView, orderStatus: OrderStatus?) {
    var color = 0
    if (orderStatus != null)
        color = when (orderStatus) {
            OrderStatus.Ordered -> R.color.order_ordered
            OrderStatus.Ready, OrderStatus.Dispatched -> R.color.order_ready
            OrderStatus.PendingReturn, OrderStatus.Returned -> R.color.order_returned
            OrderStatus.Cancelled -> R.color.order_cancelled
            OrderStatus.Delivered -> R.color.order_delivered
        }
    else
        color = R.color.order_returned
    tv.setTextColor(ContextCompat.getColor(tv.context, color))
}
