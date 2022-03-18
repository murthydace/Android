package com.local.growkart.order.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class OrderStatus(val value:String) : Parcelable {
    Ordered("Ordered"),
    Ready("Ready"),
    Dispatched("Dispatched"),
    Delivered("Delivered"),
    Returned("Returned"),
    PendingReturn("Pending return"),
    Cancelled("Cancelled")
}