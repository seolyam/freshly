// ProductViewModel.kt
package com.example.freshly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshly.models.Product
import com.example.freshly.ui.theme.ApiClient
import com.example.freshly.ui.theme.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = apiService.getProducts()
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    if (productResponse != null) {
                        _products.value = productResponse.products
                        _errorMessage.value = null
                    } else {
                        _errorMessage.value = "Failed to parse product data"
                    }
                } else {
                    _errorMessage.value = "Failed to fetch products: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }
}
