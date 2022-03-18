package com.local.growkart.order

import com.local.growkart.base.BaseMapper
import com.local.growkart.dashboard.cart.CartModel
import com.local.growkart.order.model.Order
import com.local.growkart.order.model.OrderItem
import com.local.growkart.order.model.OrderStatus
import com.local.growkart.order.model.ProductType
import com.local.growkart.util.StringUtil


class OrderMapper(val orderId: String = "", val customerId: String = "") :
    BaseMapper<List<CartModel>, Order>() {
    val mapper = OrderItemMapper()
    override fun map(source: List<CartModel>): Order {
        return Order(
            orderId,
            customerId,
            OrderStatus.Ordered,
            StringUtil.getCurrentTimeStamp(),
            getGrandTotal(source).toDouble(),
            0.00,
            getGrandTotal(source).toDouble(),
            0.00,
            0.00,
            "",
            false,
            source.get(0).product.iconUrl,
            mapper.map(source)
        )
    }

    fun getGrandTotal(cart: List<CartModel>): Float {
        var total = 0f
        cart.forEach {
            total = total.plus(it.product.price?.times(it.qty) as Float)
        }
        return total
    }

    override fun mapInverse(source: Order): List<CartModel> {
        val list = mutableListOf<CartModel>()
        source.items.map { list.add(mapper.mapInverse(it)) }
        return list
    }
}


class OrderItemMapper : BaseMapper<CartModel, OrderItem>() {
    override fun map(source: CartModel): OrderItem {
        return OrderItem(
            source.product.id,
            source.product.name.orEmpty(),
            source.qty.toDouble(),
            "",
            ((source.product.price?.times(source.qty)) ?: 0).toDouble(),
            ProductType.CONSUMABLE,
            source.product.price ?: 0f,
            source.product.iconUrl ?: ""
        )
    }

    override fun mapInverse(source: OrderItem): CartModel {
        return CartModel(source.getProduct(), source.quantity.toInt())
    }

}