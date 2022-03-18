package com.local.growkart.di.module

import com.local.growkart.dashboard.repository.CartRepository
import com.local.growkart.dashboard.repository.ICartRepository
import com.local.growkart.dashboard.repository.IProductRepository
import com.local.growkart.dashboard.repository.ProductRepository
import com.local.growkart.order.repository.IOrderRepository
import com.local.growkart.order.repository.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    fun bindCartRepository(cartRepository: CartRepository): ICartRepository

    @Binds
    fun bindProductRepository(productRepository: ProductRepository): IProductRepository

    @Binds
    fun bindOrderRepository(orderRepository: OrderRepository): IOrderRepository
}