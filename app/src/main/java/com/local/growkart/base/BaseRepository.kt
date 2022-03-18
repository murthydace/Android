package com.local.growkart.base

import androidx.lifecycle.MutableLiveData
import com.local.growkart.Resource
import com.local.growkart.ResourceState

open class BaseRepository() {
    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    fun <T> MutableLiveData<Resource<T>>.setSuccess(t: T? = null) {
        postValue(Resource(ResourceState.SUCCESS, t))
        notifyObserver()
    }

    fun <T> MutableLiveData<Resource<T>>.setError(error: String? = null) {
        postValue(Resource(ResourceState.ERROR, null, error))
        notifyObserver()
    }

    fun <T> MutableLiveData<Resource<T>>.setProgress() {
        postValue(Resource(ResourceState.LOADING))
        notifyObserver()
    }

}

