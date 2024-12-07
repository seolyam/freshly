package com.example.freshly.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val apiService: ApiService, private val tokenManager: TokenManager) : ViewModel() {

    private val cartItemsFlow = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = cartItemsFlow

    private val errorMessageFlow = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = errorMessageFlow

    fun fetchCartItemsFromBackend() {
        val token = tokenManager.getToken() ?: return
        val authHeader = "Bearer $token"

        viewModelScope.launch {
            try {
                val response = apiService.getCartItems(authHeader)
                if (response.isSuccessful) {
                    val cartItemsResponse = response.body()
                    cartItemsFlow.value = cartItemsResponse?.cartItems?.map { dto ->
                        CartItem(
                            productId = dto.productId,
                            name = dto.name,
                            price = dto.price,
                            quantity = dto.quantity,
                            imageUrl = dto.imageUrl
                        )
                    } ?: emptyList()
                } else {
                    errorMessageFlow.value = "Failed to fetch cart items: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                errorMessageFlow.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun addItemToRemoteCart(productId: Int, quantity: Int, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        val token = tokenManager.getToken() ?: return
        val authHeader = "Bearer $token"
        viewModelScope.launch {
            try {
                val request = AddCartItemRequest(productId = productId, quantity = quantity)
                val response = apiService.addCartItem(authHeader, request)
                if (response.isSuccessful && response.body()?.success == true) {
                    fetchCartItemsFromBackend()
                    onSuccess()
                } else {
                    onError("Failed to add item to cart: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onError("An error occurred: ${e.message}")
            }
        }
    }

    fun addItem(item: CartItem) {
        // Local addition (optimistic), backend is updated separately by addItemToRemoteCart
        val currentItems = cartItemsFlow.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.productId == item.productId }

        if (existingItemIndex != -1) {
            val existingItem = currentItems[existingItemIndex]
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + item.quantity)
            currentItems[existingItemIndex] = updatedItem
        } else {
            currentItems.add(item)
        }

        cartItemsFlow.value = currentItems
    }

    fun incrementItemQuantity(item: CartItem) {
        updateCartItemQuantityRemote(item.productId, item.quantity + 1)
    }

    fun decrementItemQuantity(item: CartItem) {
        if (item.quantity > 1) {
            updateCartItemQuantityRemote(item.productId, item.quantity - 1)
        } else {
            // Setting quantity to 0 removes it
            updateCartItemQuantityRemote(item.productId, 0)
        }
    }

    fun removeItem(item: CartItem) {
        // Set quantity to 0 to remove
        updateCartItemQuantityRemote(item.productId, 0)
    }

    private fun updateCartItemQuantityRemote(productId: Int, newQuantity: Int) {
        val token = tokenManager.getToken() ?: return
        val authHeader = "Bearer $token"

        viewModelScope.launch {
            try {
                val request = UpdateCartQuantityRequest(productId, newQuantity)
                val response = apiService.updateCartItemQuantity(authHeader, request)
                if (response.isSuccessful && response.body()?.success == true) {
                    fetchCartItemsFromBackend()
                } else {
                    errorMessageFlow.value = "Failed to update quantity: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                errorMessageFlow.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun getTotalPrice(): Double {
        return cartItemsFlow.value.sumOf { it.price * it.quantity }
    }

    fun clearCart() {
        cartItemsFlow.value = emptyList()
    }

    fun checkout(onSuccess: (Int) -> Unit = {}, onError: (String) -> Unit = {}) {
        val token = tokenManager.getToken() ?: return
        val authHeader = "Bearer $token"
        viewModelScope.launch {
            try {
                val response = apiService.checkout(authHeader)
                if (response.isSuccessful && response.body()?.success == true) {
                    // Cart cleared on backend side, reflect locally
                    cartItemsFlow.value = emptyList()
                    onSuccess(response.body()?.orderId ?: 0)
                } else {
                    onError("Failed to checkout: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onError("An error occurred: ${e.message}")
            }
        }
    }
}
