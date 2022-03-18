package com.local.growkart.order.repository

import androidx.lifecycle.MutableLiveData
import com.local.growkart.Resource
import com.local.growkart.order.model.Order

interface IOrderRepository {
    fun getOrders(): MutableLiveData<Resource<List<Order>>>

    fun getOrder(orderId: String, order: MutableLiveData<Resource<Order>>)

    fun cancelOrder(orderId:String,  order: MutableLiveData<Resource<Order>>)
}