package com.local.growkart.order.model

import android.os.Parcelable
import com.local.growkart.dashboard.model.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    val productId: String = "",
    val itemName: String = "",
    val quantity: Double = 0.0,
    val tamilname: String = "",
    val totalPrice: Double = 0.0,
    val type: ProductType = ProductType.CONSUMABLE,
    var unitPrice: Float = 0.0F,
    var iconUrl: String = ""
) : Parcelable {
    constructor() : this("")

    fun getProduct() = Product(productId, itemName, iconUrl, unitPrice)
}

enum class ProductType {
    CONSUMABLE, DECORATIVE, GROCERY
}