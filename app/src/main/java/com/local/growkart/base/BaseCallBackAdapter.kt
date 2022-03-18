package com.local.growkart

import androidx.recyclerview.widget.RecyclerView
import com.local.growkart.dashboard.adapter.ProductAdapter

abstract class BaseCallBackAdapter<T : RecyclerView.ViewHolder>():RecyclerView.Adapter<T>()

private var baseCallBack: ListSelectionListener? = null
fun setCallBack(callBack: ListSelectionListener) {
    baseCallBack = callBack
}

fun getCallBack() = baseCallBack

interface ListSelectionListener {
    fun onItemSelected()
}