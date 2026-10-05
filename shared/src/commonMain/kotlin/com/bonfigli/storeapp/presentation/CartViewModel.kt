package com.bonfigli.storeapp.presentation

import androidx.lifecycle.ViewModel
import com.bonfigli.storeapp.domain.CartItem
import com.bonfigli.storeapp.domain.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addProduct(product: Product) {
        _cartItems.value = _cartItems.value.toMutableList().apply {
            val existingIndex = indexOfFirst { it.product.id == product.id }
            if (existingIndex != -1) {
                val currentItem = this[existingIndex]
                this[existingIndex] = currentItem.copy(quantity = currentItem.quantity + 1)
            } else {
                add(CartItem(product = product, quantity = 1))
            }
        }
    }

    fun incrementQuantity(productId: Int) {
        _cartItems.value = _cartItems.value.map { item ->
            if (item.product.id == productId) {
                item.copy(quantity = item.quantity + 1)
            } else {
                item
            }
        }
    }

    fun decrementQuantity(productId: Int) {
        _cartItems.value = _cartItems.value.mapNotNull { item ->
            if (item.product.id == productId) {
                if (item.quantity > 1) {
                    item.copy(quantity = item.quantity - 1)
                } else {
                    null
                }
            } else {
                item
            }
        }
    }

    fun removeItem(productId: Int) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.totalPrice }
    }

    fun getTotalItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
}
