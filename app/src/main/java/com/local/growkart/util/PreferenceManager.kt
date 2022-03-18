package com.local.growkart.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.local.growkart.dashboard.cart.CartModel

class PreferenceManager constructor(val sharedPreference: SharedPreferences) {

    val gson by lazy { Gson() }
    private var cart: MutableList<CartModel> = mutableListOf()

    init {
        if (sharedPreference.doObjectExist(CART))
            cart = sharedPreference.getCartList(CART).toMutableList()
    }

    fun getCart(): List<CartModel> = cart

    fun SharedPreferences.doObjectExist(key: String) =
        !this.getString(key, "").isNullOrEmpty() ?: false

    private fun SharedPreferences.getCartList(key: String): List<CartModel> {
        val json: String? = this.getString(key, "")
        val obj: List<CartModel> =
            gson.fromJson(json, object : TypeToken<List<CartModel>>() {}.type)
        return obj
    }

    private inline fun <reified T> SharedPreferences.getList(key: String): List<T> {
        val json: String? = this.getString(key, "")
        val obj: List<T> = gson.fromJson<List<T>>(json, object : TypeToken<List<T>>() {}.type)
        return obj
    }


    private inline fun <reified T> SharedPreferences.setObject(key: String, obj: T) {
        val json = gson.toJson(obj)
        val prefsEditor = this.edit()
        prefsEditor.putString(key, json)
        prefsEditor.apply()
    }

    fun addProductToCart(cartModel: CartModel) {
        cart.add(cartModel)
        sharedPreference.setObject(CART, cart)
    }

    fun removeProductFromCart(cartModel: CartModel) {
        cart.remove(cartModel)
        sharedPreference.setObject(CART, cart)
    }

    fun updateProductInCart(cartModel: CartModel) {
        cart.find { it.product.id == cartModel.product.id }?.qty = cartModel.qty
        sharedPreference.setObject(CART, cart)
    }

    fun addProducts(items: List<CartModel>) {
        cart.addAll(items)
        sharedPreference.setObject(CART, cart)
    }

    fun clearCart() {
        cart.clear()
        sharedPreference.setObject(CART, cart)
    }

    companion object {
        const val PRODUCT = "PRODUCT"
        const val CART = "CART"
    }

}