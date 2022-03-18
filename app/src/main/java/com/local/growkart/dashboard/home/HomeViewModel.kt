package com.local.growkart.dashboard.home

import androidx.lifecycle.*
import com.local.growkart.base.BaseViewModel
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.dashboard.model.Product
import com.local.growkart.dashboard.repository.CartRepository
import com.local.growkart.dashboard.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val cartRepo: CartRepository
) : BaseViewModel() {

    private lateinit var cart: LiveData<MutableList<CartModel>>
    private var products = MutableLiveData<MutableList<Product>>()
    val progressBarVisibility = MutableLiveData<Boolean>()

    fun init() {
        progressBarVisibility.value = true
        products = repo.getAllProducts()
        cart = cartRepo.getCartFromRepo()
    }

    fun getAllProducts() = products
    fun getCurrentCart(): LiveData<MutableList<CartModel>> = cart


    fun addProductToCart(cartModel: CartModel) {
        progressBarVisibility.value = true
        val doProductExist = cart.value?.find {
            it.product.id == cartModel.product.id
        }
        if (doProductExist != null)
            cartRepo.updateProduct(cartModel)
        else {
            cartRepo.addProduct(cartModel)
        }
        progressBarVisibility.value = false
    }

    fun removeProductFromCart(product: Product) {
        progressBarVisibility.value = true
        cartRepo.removeProduct(product)
        progressBarVisibility.value = false
    }

    fun hideProgress() {
        progressBarVisibility.value = false
    }
}