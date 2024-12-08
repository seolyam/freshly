package com.example.freshly.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(private val apiService: ApiService, private val tokenManager: TokenManager): ViewModel() {
    private val ordersFlow = MutableStateFlow<List<OrderDto>>(emptyList())
    val orders: StateFlow<List<OrderDto>> get() = ordersFlow

    private val loadingFlow = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = loadingFlow

    private val errorFlow = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = errorFlow

    fun fetchOrders() {
        val token = tokenManager.getToken() ?: run {
            errorFlow.value = "User not authenticated"
            return
        }
        val authHeader = "Bearer $token"

        viewModelScope.launch {
            loadingFlow.value = true
            try {
                val response = apiService.getOrders(authHeader)
                if (response.isSuccessful && response.body()?.success == true) {
                    ordersFlow.value = response.body()?.orders ?: emptyList()
                    errorFlow.value = null
                } else {
                    errorFlow.value = "Failed to fetch orders"
                }
            } catch (e: Exception) {
                errorFlow.value = "An error occurred: ${e.message}"
            } finally {
                loadingFlow.value = false
            }
        }
    }
}
