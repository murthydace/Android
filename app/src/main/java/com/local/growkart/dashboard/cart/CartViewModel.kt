package com.local.growkart.dashboard.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.local.growkart.base.BaseViewModel
import com.local.growkart.dashboard.model.Product
import com.local.growkart.dashboard.repository.CartRepository
import com.local.growkart.order.OrderMapper
import com.local.growkart.order.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class CartViewModel @Inject constructor(
     val repo: CartRepository,
     val auth: FirebaseAuth
) : BaseViewModel() {
    private lateinit var cart: MutableLiveData<MutableList<CartModel>>
    private lateinit var cartTotal: LiveData<Float>
    private var isUserLoggedIn = MutableLiveData<Boolean>()

    fun init() {
        cart = repo.getCartFromRepo()
        cartTotal = repo.calculateTotalCost()
        isUserLoggedIn.value = auth.currentUser != null
    }

    fun changeProductQuantity(cartModel: CartModel) {
        val doProductExist = cart.value?.find {
            it.product.id == cartModel.product.id
        }
        if (doProductExist != null)
            repo.updateProduct(cartModel)
        else {
            repo.addProduct(cartModel)
        }
    }

    fun getCart(): LiveData<MutableList<CartModel>> = cart

    fun deleteProductFromCart(product: Product) = repo.removeProduct(product)

    fun getTotalcost(): LiveData<Float> = cartTotal


    fun emptyCart() {
        repo.emptyCart()

    }

    fun createOrder(successAction: () -> Unit, failureAction: () -> Unit) {

        if (auth.currentUser != null)
            repo.createOrder(successAction, failureAction)
        else
            isUserLoggedIn.value = false
    }

    fun getUserSessionStatus() = isUserLoggedIn

    fun convertCartToOrder(): Order {
        val mapper = OrderMapper()
        val cartItems =
            getCart().value?.toList()!! //Place order is only shown when items are present, so asserting null
        return mapper.map(cartItems)
    }

}