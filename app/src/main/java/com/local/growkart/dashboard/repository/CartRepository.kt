package com.local.growkart.dashboard.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.local.growkart.base.BaseRepository
import com.local.growkart.GrowKartActivity
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.dashboard.model.Product
import com.local.growkart.order.OrderMapper
import com.local.growkart.order.model.Order
import com.local.growkart.order.model.OrderItem
import com.local.growkart.util.DatabaseUtil
import com.local.growkart.util.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val currentUser: FirebaseUser?,
    private val preferenceManager: PreferenceManager
) : BaseRepository(), ICartRepository {

    private val cart = MutableLiveData<MutableList<CartModel>>()
    private val cartTotal = MutableLiveData<Float>()

    override fun getCartFromRepo(): MutableLiveData<MutableList<CartModel>> {
        if (cart.value == null)
            cart.value = preferenceManager.getCart().toMutableList()
        else
            cart
        Log.d(GrowKartActivity.TAG, "cart Repo getCart ${cart.value?.size}")
        calculateTotalCost()
        return cart
    }


    override fun addProduct(cartModel: CartModel) {
        Log.d(GrowKartActivity.TAG, "cart Repo addProduct ${cartModel.product.name}")
//        if (cart.value == null)
//            cart.value = mutableListOf()
        addProductToCart(cartModel)
        cart.notifyObserver()
        calculateTotalCost()
    }

    override fun removeProduct(product: Product) {
//        if (cart.value == null)
//            cart.value = mutableListOf()
        val cartmodel =
            cart.value?.find { model -> model.product.id == product.id }
        removeProductFromCart(product)
        cart.notifyObserver()
        calculateTotalCost()
    }

    override fun updateProduct(cartModel: CartModel) {
//        if (cart.value == null)
//            cart.value = mutableListOf()

//        cart.value?.find { model -> model.product.id == cartModel.product.id }?.qty = cartModel.qty
        updateProductInCart(cartModel)
        cart.notifyObserver()
        calculateTotalCost()
    }

    override fun addProducts(items: List<CartModel>) {
        addProductsToCart(items)
        cart.notifyObserver()
        calculateTotalCost()

    }

    //local storage updates
    private fun addProductToCart(cartModel: CartModel) {
        cart.value?.add(cartModel)
        preferenceManager.addProductToCart(cartModel)
    }

    private fun addProductsToCart(items: List<CartModel>) {
        cart.value?.addAll(items)
        preferenceManager.addProducts(items)
    }

    private fun removeProductFromCart(product: Product) {
        val cartmodel =
            cart.value?.find { model -> model.product.id == product.id }
        if (cartmodel != null) {
            cart.value?.remove(cartmodel)
            preferenceManager.removeProductFromCart(cartmodel)
        }
    }

    private fun updateProductInCart(cartModel: CartModel) {
        cart.value?.find { model -> model.product.id == cartModel.product.id }?.qty = cartModel.qty
        preferenceManager.updateProductInCart(cartModel)
    }

    override fun calculateTotalCost(): MutableLiveData<Float> {
        var total = 0f
        cart.value?.forEach {
            total = total.plus(it.product.price?.times(it.qty) as Float)
        }
        cartTotal.value = total
        return cartTotal
    }

    override fun emptyCart() {
        cart.value = arrayListOf()
        preferenceManager.clearCart()
        cart.notifyObserver()
    }

    override fun createOrder(
        successAction: () -> Unit,
        failureAction: () -> Unit,
        orderId: String?
    ) {
        if (orderId == null)
            createOrderInDB(successAction, failureAction)
        else
            updateOrderInDB(successAction, failureAction, orderId)
    }

    override fun createOrderInDB(
        successAction: () -> Unit,
        failureAction: () -> Unit,
        orderId: String?
    ) {
        val documentId =
            db.collection(DatabaseUtil.FireStore.Collections.ORDER).document().id
        val orderMapper = OrderMapper(documentId, currentUser?.phoneNumber.orEmpty())
        val order = cart.value?.let { orderMapper.map(it) }
        order.let {
            db.collection(DatabaseUtil.FireStore.Collections.ORDER)
                .document(orderId ?: documentId)
                .set(order!!)
                .addOnSuccessListener {
                    Log.d(GrowKartActivity.TAG, "Order with ${documentId} is created ")
                    successAction()
                }
                .addOnFailureListener {
                    Log.d(
                        GrowKartActivity.TAG,
                        "Order with ${documentId} has failed \n ${it.printStackTrace()}"
                    )
                    failureAction()
                }
        }
    }


    override fun updateOrderInDB(
        successAction: () -> Unit,
        failureAction: () -> Unit,
        orderId: String?
    ) {
        val orderMapper = OrderMapper(orderId!!, currentUser?.phoneNumber.orEmpty())
        val order = cart.value?.let { orderMapper.map(it) }
        order.let {
            db.collection(DatabaseUtil.FireStore.Collections.ORDER)
                .document(orderId)
                .update(getUpdatedCart(order))
                .addOnSuccessListener {
                    Log.d(GrowKartActivity.TAG, "Order with ${orderId} is Updated ")
                    successAction()
                }
                .addOnFailureListener {
                    Log.d(
                        GrowKartActivity.TAG,
                        "Order with ${orderId} has failed \n ${it.printStackTrace()}"
                    )
                    failureAction()
                }
        }


    }

    private fun getUpdatedCart(order: Order?): MutableMap<String, Any> {
        val updateCart = mutableMapOf<String, Any>()
        order?.let {

            updateCart.put("items", order.items)
        }
        return updateCart
    }
}