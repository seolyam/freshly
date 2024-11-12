package com.example.freshly.ui.theme

import ApiService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


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
                val response = apiService.register(
                    RegisterRequest(first_name = firstName, last_name = lastName, email = email, password = password)
                )
                if (response.isSuccessful) {
                    // After successful registration, perform login
                    login(email, password) {
                        onSuccess()
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
