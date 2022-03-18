package com.local.growkart.util

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.local.growkart.GrowKartApp

class DatabaseUtil {
    class FireStore {
        class Collections {
            companion object {
                val userid: String by lazy {
                    Firebase.auth.currentUser?.phoneNumber ?: ""
                }
                const val PRODUCT = "products"
                const val ORDER_ITEMS = "orderItems"
                const val PROFILE = "profiles"
                val ORDER = "orders/users/$userid"
            }
        }
        class Fields{
            companion object{
                const val ORDER_ID = "orderId"
            }
        }
    }
    companion object{
        const val PREF_NAME ="GrowKartPref"
    }
}