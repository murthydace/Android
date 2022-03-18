package com.local.growkart.dashboard.cart

import android.os.Parcelable
import com.local.growkart.dashboard.model.Product
import kotlinx.parcelize.Parcelize

@Parcelize
class CartModel(val product: Product, var qty: Int) : Parcelable