package com.local.growkart.dashboard.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.local.growkart.base.BaseRepository
import com.local.growkart.GrowKartActivity
import com.local.growkart.dashboard.model.Product
import com.local.growkart.util.DatabaseUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val fireStore: FirebaseFirestore) :
    BaseRepository(), IProductRepository {
    val products = MutableLiveData<MutableList<Product>>()

    init {
        fireStore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    //database entries and if null load from network
    override fun getAllProducts(): MutableLiveData<MutableList<Product>> {
        loadProducts()
        return products
    }

    override fun loadProducts() {

        fireStore.collection(DatabaseUtil.FireStore.Collections.PRODUCT).get()
            .addOnSuccessListener {
                val productsFromStore = it.documents
                Log.d(GrowKartActivity.TAG, productsFromStore.size.toString())
                try {
                    if (productsFromStore.isNotEmpty()) {
                        val productList = mutableListOf<Product>()
                        productsFromStore.forEach {
                            val product = it.toObject(Product::class.java)
                            Log.d(GrowKartActivity.TAG, "product ${product?.name.orEmpty()}")
                            productList.add(product!!)
                        }
                        products.value = productList
                    }
                } catch (e: Throwable) {
                    Log.d(GrowKartActivity.TAG, "Parse error ${e.message}")
                    e.printStackTrace()
                }

            }
            .addOnFailureListener {
                Log.d(GrowKartActivity.TAG, "failed ${it.message}")
                it.printStackTrace()
            }
    }
//        productDao.getAllProducts() // in future we can check for

    override fun insertProduct(product: Product) {
        if (products.value == null) {
            products.value = mutableListOf()
        }
        products.value?.add(product)
        products.notifyObserver()


    }

    override fun deleteProduct(product: Product) {
        if (products.value == null) {
            products.value = mutableListOf()
        }
        products.value?.remove(product)
        products.notifyObserver()
    }

    override fun insertMultipleProducts(allProduct: List<Product>) {
        if (products.value == null) {
            products.value = mutableListOf()
        }
        products.value?.addAll(allProduct)
        products.notifyObserver()
    }

//    fun getProduct(productId:String): Product{
//        fireStore.collection(DatabaseUtil.FireStore.Collections.PRODUCT)
//            .document(productId)
//            .get()
//            .addOnSuccessListener {
//
//            }
//            .addOnFailureListener {
//                Log.d(GrowKartActivity.TAG, "failed ${it.message}")
//                it.printStackTrace()
//            }
//    }
}