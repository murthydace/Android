package com.local.growkart.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.local.growkart.BuildConfig
import com.local.growkart.Resource
import com.local.growkart.ResourceState


open class BaseViewModel : ViewModel() {

    fun onError(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
    }

    fun <T> MutableLiveData<Resource<T>>.setSuccess(data: T? = null) =
        postValue(Resource(ResourceState.SUCCESS, data))

    fun <T> MutableLiveData<Resource<T>>.setLoading() =
        postValue(Resource(ResourceState.LOADING, value?.data))

    fun <T> MutableLiveData<Resource<T>>.setError(message: String? = null) =
        postValue(Resource(ResourceState.ERROR, value?.data, message))

    fun <T> MutableLiveData<Resource<T>>.setError(message: String? = null, data: T? = null) =
        postValue(Resource(ResourceState.ERROR, data, message))
}