// CartViewModel.kt
package com.example.freshly.ui.theme

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

data class CartItem(val name: String, val quantity: Int, val price: String)

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: SnapshotStateList<CartItem> get() = _cartItems

    fun addItem(item: CartItem) {
        _cartItems.add(item)
    }
}
