package com.example.freshly.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems

    fun addItem(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.name == item.name }

        if (existingItemIndex != -1) {
            // Update the quantity by incrementing it by 1
            val existingItem = currentItems[existingItemIndex]
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            currentItems[existingItemIndex] = updatedItem
        } else {
            // Add a new item with the specified quantity
            currentItems.add(item)
        }

        _cartItems.value = currentItems
    }

    fun incrementItemQuantity(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.name == item.name }

        if (existingItemIndex != -1) {
            val existingItem = currentItems[existingItemIndex]
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            currentItems[existingItemIndex] = updatedItem
            _cartItems.value = currentItems
        }
    }

    fun decrementItemQuantity(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.name == item.name }

        if (existingItemIndex != -1) {
            val existingItem = currentItems[existingItemIndex]
            if (existingItem.quantity > 1) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity - 1)
                currentItems[existingItemIndex] = updatedItem
            } else {
                // Remove the item if quantity is 1
                currentItems.removeAt(existingItemIndex)
            }
            _cartItems.value = currentItems
        }
    }

    fun removeItem(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeIf { it.name == item.name }
        _cartItems.value = currentItems
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.quantity * it.price }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
