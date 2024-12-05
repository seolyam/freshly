// UserViewModel.kt
package com.example.freshly.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Import the request and response models
import com.example.freshly.ui.theme.RegisterRequest
import com.example.freshly.ui.theme.LoginRequest
import com.example.freshly.ui.theme.RegisterResponse
import com.example.freshly.ui.theme.LoginResponse

class UserViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> get() = _userInfo

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    data class UserInfo(
        var firstName: String = "",
        var middleInitial: String = "",
        var lastName: String = "",
        var email: String = "",
        var birthdate: String = "",
        var address: String = "",
        var token: String = ""
    )

    init {
        // Load the token from TokenManager when the ViewModel is initialized
        val savedToken = tokenManager.getToken()
        if (savedToken != null) {
            _userInfo.value = _userInfo.value.copy(token = savedToken)
            // Fetch user profile if needed
            fetchUserProfile()
        }
    }

    fun getToken(): String? {
        return tokenManager.getToken()
    }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    first_name = firstName,
                    last_name = lastName,
                    email = email,
                    password = password
                )
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        // After successful registration, perform login
                        login(email, password) {
                            onSuccess()
                        }
                    } else {
                        onError("Registration failed: ${responseBody?.message}")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    onError("Registration failed: $errorBody")
                }
            } catch (e: Exception) {
                onError("An error occurred: ${e.message}")
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        responseBody.token?.let { token ->
                            tokenManager.saveToken(token)
                            _userInfo.value = _userInfo.value.copy(email = email, token = token)
                            _errorMessage.value = ""
                            // Fetch user profile after login
                            fetchUserProfile()
                            onSuccess()
                        } ?: run {
                            _errorMessage.value = "Login failed: Token not received"
                        }
                    } else {
                        _errorMessage.value = responseBody?.message ?: "Login failed"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Login failed: $errorBody"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun fetchUserProfile(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getBearerToken()
                if (token != null) {
                    val response = apiService.getProfile(token)
                    if (response.isSuccessful) {
                        val profile = response.body()
                        if (profile != null) {
                            _userInfo.value = _userInfo.value.copy(
                                firstName = profile.firstName ?: "",
                                middleInitial = profile.middleInitial ?: "",
                                lastName = profile.lastName ?: "",
                                email = profile.email ?: "",
                                birthdate = profile.birthdate ?: "",
                                address = profile.address ?: ""
                            )
                            onSuccess()
                        } else {
                            _errorMessage.value = "Failed to parse profile data"
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        _errorMessage.value = "Failed to fetch profile: $errorBody"
                    }
                } else {
                    _errorMessage.value = "User not authenticated"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun updateUserProfile(
        firstName: String,
        middleInitial: String,
        lastName: String,
        email: String, // Include email parameter
        birthdate: String,
        address: String,
        password: String? = null,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getBearerToken()
                if (token != null) {
                    val request = UserProfileRequest(
                        firstName = firstName,
                        middleInitial = middleInitial,
                        lastName = lastName,
                        email = email, // Include email
                        birthdate = birthdate,
                        address = address,
                        password = password
                    )
                    val response = apiService.updateProfile(token, request)
                    if (response.isSuccessful) {
                        val profile = response.body()
                        if (profile != null) {
                            _userInfo.value = _userInfo.value.copy(
                                firstName = profile.firstName ?: "",
                                middleInitial = profile.middleInitial ?: "",
                                lastName = profile.lastName ?: "",
                                email = profile.email ?: "",
                                birthdate = profile.birthdate ?: "",
                                address = profile.address ?: ""
                            )
                            onSuccess()
                        } else {
                            onError("Failed to parse updated profile data")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        onError("Failed to update profile: $errorBody")
                    }
                } else {
                    onError("User not authenticated")
                }
            } catch (e: Exception) {
                onError("An error occurred: ${e.message}")
            }
        }
    }

    fun logout() {
        tokenManager.clearToken()
        _userInfo.value = UserInfo()
    }
}
