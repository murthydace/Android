package com.local.growkart.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.local.growkart.Resource
import com.local.growkart.ResourceState
import com.local.growkart.base.BaseViewModel
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.dashboard.cart.CartViewModel
import com.local.growkart.dashboard.repository.CartRepository
import com.local.growkart.order.OrderMapper
import com.local.growkart.order.model.Order
import com.local.growkart.order.model.OrderStatus
import com.local.growkart.util.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EditOrderViewModel @Inject constructor(
    private val repo: CartRepository,
    private val auth: FirebaseAuth
) : BaseViewModel() {
    val order = MutableLiveData<Order>()
    private val mapper = OrderMapper()
    val error = MutableLiveData<Error>()

    //A method to virtually create a cart with the order items so that the state will persist.
    fun initCartWithOrderItems(order: Order) {
        this.order.value = order
        repo.addProducts(mapper.mapInverse(order))
    }

    override fun onCleared() {
        super.onCleared()
        repo.emptyCart()
    }

    fun updateOrder(successAction: () -> Unit, failureAction: () -> Unit) {
        //assuming orders will load only when user is logged in
        repo.createOrder(successAction, failureAction, order.value?.orderId)
    }
}