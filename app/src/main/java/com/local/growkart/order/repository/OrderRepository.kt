package com.local.growkart.order.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.local.growkart.base.BaseRepository
import com.local.growkart.Resource
import com.local.growkart.order.model.Order
import com.local.growkart.order.model.OrderStatus
import com.local.growkart.util.DatabaseUtil
import java.lang.Exception
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val user: FirebaseUser?,
    private val db: FirebaseFirestore,
) : IOrderRepository, BaseRepository() {
    private val userOrder = MutableLiveData<Resource<List<Order>>>()

    override fun getOrders(): MutableLiveData<Resource<List<Order>>> {
        userOrder.setProgress()
        db.collection(DatabaseUtil.FireStore.Collections.ORDER)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    try {
                        val currentUserOrders = it.toObjects(Order::class.java)
                        userOrder.setSuccess(currentUserOrders)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        userOrder.setError("Unable to fetch orders at the moment! try again later")
                    }
                } else {
                    userOrder.setError("No Orders")
                }
            }
            .addOnFailureListener {
                userOrder.setError("Failed to fetch orders. Try again later")
            }
        return userOrder
    }


    override fun getOrder(orderId: String, order: MutableLiveData<Resource<Order>>) {
        db.collection(DatabaseUtil.FireStore.Collections.ORDER)
            .document(orderId)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val filteredOrder = it.toObject(Order::class.java)
                    order.setSuccess(filteredOrder)
                } else {
                    order.setError("Order not found")
                }
            }
            .addOnFailureListener {
                order.setError("Unable to fetch details")
            }
    }

    override fun cancelOrder(orderId: String, order: MutableLiveData<Resource<Order>>) {
        if (user?.phoneNumber != null)
            db.collection(DatabaseUtil.FireStore.Collections.ORDER)
                .document(orderId)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val filteredOrder = it.toObject(Order::class.java)
                        filteredOrder?.status = OrderStatus.Cancelled
                        updateOrder(filteredOrder)
                    } else {
                        order.setError("Order doesnt exist")
                    }
                }
                .addOnFailureListener {
                    order.setError("Order cannot be updated!")
                }
    }

    private fun updateOrder(order: Order?) {
        order.let {
            db.collection(DatabaseUtil.FireStore.Collections.ORDER)
                .document(order!!.orderId)
                .set(order)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
    }


    sealed class OrderExceptions : Exception() {
        object NoOrderException : OrderExceptions()
        class OrderFetchErrorException(throwable: Throwable) : OrderExceptions()
    }
}