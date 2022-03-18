package com.local.growkart.base

abstract class BaseMapper<T, R>() {
    abstract fun map(source: T): R
    fun map(source: List<T>): List<R> {
        val mappedList = mutableListOf<R>()
        source.forEach { t ->
            mappedList.add(map(t))
        }
        return mappedList
    }

    abstract fun mapInverse(source: R): T
    fun mapInverse(source: List<R>): List<T> {
        val mappedList = mutableListOf<T>()
        source.forEach { t ->
            mappedList.add(mapInverse(t))
        }
        return mappedList
    }
}