package com.local.growkart.dashboard.repository

import androidx.lifecycle.MutableLiveData
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.dashboard.model.Product

interface ICartRepository {
    fun getCartFromRepo(): MutableLiveData<MutableList<CartModel>>
    fun addProduct(cartModel: CartModel)
    fun removeProduct(product: Product)
    fun updateProduct(cartModel: CartModel)
    fun calculateTotalCost(): MutableLiveData<Float>
    fun emptyCart()
    fun createOrder(successAction: () -> Unit, failureAction: () -> Unit, orderID: String? = null)
    fun createOrderInDB(
        successAction: () -> Unit,
        failureAction: () -> Unit,
        orderID: String? = null
    )

    fun addProducts(items: List<CartModel>)
    fun updateOrderInDB(successAction: () -> Unit, failureAction: () -> Unit, orderId: String?)
}