package com.local.growkart.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.local.growkart.base.BaseViewModel
import com.local.growkart.Resource
import com.local.growkart.ResourceState
import com.local.growkart.order.model.Order
import com.local.growkart.order.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repo: OrderRepository,
    private val auth: FirebaseAuth
) : BaseViewModel() {

    private val orders by lazy {
        repo.getOrders()
    }
    private val emptyOrder by lazy {
        MutableLiveData(Resource(ResourceState.ERROR, emptyList<Order>(),"Please login to see orders!"))
    }

    private val _isUserLoggedIn by lazy {
        auth.currentUser != null
    }
    val showProgress = MutableLiveData<Boolean>()
    val showOrders = MutableLiveData<Boolean>()

    fun getOrders(): LiveData<Resource<List<Order>>> {
        return if (_isUserLoggedIn)
            orders
        else
            emptyOrder

    }

    fun isUserLoggedIn() = _isUserLoggedIn

}