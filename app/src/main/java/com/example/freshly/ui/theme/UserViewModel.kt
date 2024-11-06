package com.example.freshly.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import parseErrorResponse

data class UserInfo(
    var username: String = "",
    var email: String = "",
    var token: String = "",
    var firstName: String = "",
    var middleInitial: String = "",
    var lastName: String = "",
    var birthdate: String = "",
    var address: String = ""
)

class UserViewModel(private val tokenManager: TokenManager) : ViewModel() {
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> get() = _userInfo

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun register(
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = apiService.register(RegisterRequest(username, email, password))
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        it.token?.let { token ->
                            // Save the token
                            tokenManager.saveToken(token)
                            // Update user info
                            _userInfo.value = _userInfo.value.copy(username = username, token = token)
                            _errorMessage.value = ""
                            onSuccess()
                        } ?: onError("Registration failed: No token received")
                    } ?: onError("Registration failed: No response body")
                } else {
                    val errorMessage = parseErrorResponse(response)
                    onError("Registration failed: $errorMessage")
                }
            } catch (e: Exception) {
                onError("An error occurred: ${e.message}")
            }
        }
    }

    // Login Function
    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        responseBody.token?.let { token ->
                            tokenManager.saveToken(token)
                            _userInfo.value = _userInfo.value.copy(username = username, token = token)
                            _errorMessage.value = ""
                            onSuccess()
                        } ?: run {
                            _errorMessage.value = responseBody.message ?: "Login failed"
                        }
                    }
                } else {
                    // Handle error response
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Login failed: $errorBody"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    // Fetch User Profile
    fun fetchUserProfile(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token != null) {
                    val response = apiService.getProfile("Bearer $token")
                    if (response.isSuccessful) {
                        val profile = response.body()
                        if (profile != null) {
                            _userInfo.value = _userInfo.value.copy(
                                username = profile.username ?: _userInfo.value.username,
                                email = profile.email ?: _userInfo.value.email,
                                firstName = profile.firstName ?: "",
                                middleInitial = profile.middleInitial ?: "",
                                lastName = profile.lastName ?: "",
                                birthdate = profile.birthdate ?: "",
                                address = profile.address ?: ""
                            )
                            onSuccess()
                        }
                    } else {
                        _errorMessage.value = "Failed to fetch profile"
                    }
                } else {
                    _errorMessage.value = "User not authenticated"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    // Update User Profile
    fun updateUserProfile(
        firstName: String,
        middleInitial: String,
        lastName: String,
        birthdate: String,
        address: String,
        password: String? = null,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token != null) {
                    val request = UserProfileRequest(
                        firstName = firstName,
                        middleInitial = middleInitial,
                        lastName = lastName,
                        birthdate = birthdate,
                        address = address,
                        password = password
                    )
                    val response = apiService.updateProfile("Bearer $token", request)
                    if (response.isSuccessful) {
                        val profile = response.body()
                        if (profile != null) {
                            _userInfo.value = _userInfo.value.copy(
                                firstName = profile.firstName ?: "",
                                middleInitial = profile.middleInitial ?: "",
                                lastName = profile.lastName ?: "",
                                birthdate = profile.birthdate ?: "",
                                address = profile.address ?: ""
                            )
                            onSuccess()
                        }
                    } else {
                        onError("Failed to update profile")
                    }
                } else {
                    onError("User not authenticated")
                }
            } catch (e: Exception) {
                onError("An error occurred: ${e.message}")
            }
        }
    }

    // Logout Function
    fun logout() {
        tokenManager.clearToken()
        _userInfo.value = UserInfo()
    }
}