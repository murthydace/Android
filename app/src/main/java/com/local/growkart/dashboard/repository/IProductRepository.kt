package com.local.growkart.dashboard.repository

import androidx.lifecycle.MutableLiveData
import com.local.growkart.dashboard.model.Product

interface IProductRepository {
    //database entries and if null load from network
    fun getAllProducts(): MutableLiveData<MutableList<Product>>
    fun loadProducts()
    fun insertProduct(product: Product)
    fun deleteProduct(product: Product)
    fun insertMultipleProducts(allProduct: List<Product>)
}