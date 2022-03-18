package com.local.growkart

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.local.growkart.dashboard.repository.CartRepository
import com.local.growkart.dashboard.repository.ProductRepository
import com.local.growkart.util.NotificationUtil
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltAndroidApp
class GrowKartApp : Application()
