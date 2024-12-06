package com.example.freshly.ui.theme

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
        var contactNumber: String = "",
        var token: String = ""
    )

    init {
        val savedToken = tokenManager.getToken()
        if (savedToken != null) {
            _userInfo.value = _userInfo.value.copy(token = savedToken)
            fetchUserProfile()
        }
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
                        login(email, password, onSuccess)
                    } else {
                        onError("Registration failed: ${responseBody?.message}")
                    }
                } else {
                    onError("Registration failed: ${response.errorBody()?.string()}")
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
                            fetchUserProfile(onSuccess)
                        }
                    } else {
                        _errorMessage.value = responseBody?.message ?: "Login failed"
                    }
                } else {
                    _errorMessage.value = "Login failed: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun fetchUserProfile(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token != null) {
                    val authHeader = "Bearer $token"
                    val response = apiService.getProfile(authHeader)
                    if (response.isSuccessful) {
                        val profile = response.body()
                        if (profile != null) {
                            _userInfo.value = _userInfo.value.copy(
                                firstName = profile.firstName ?: "",
                                middleInitial = profile.middleInitial ?: "",
                                lastName = profile.lastName ?: "",
                                email = profile.email ?: "",
                                birthdate = profile.birthdate ?: "",
                                address = profile.address ?: "",
                                contactNumber = profile.contactNumber ?: ""
                            )
                            onSuccess()
                        } else {
                            _errorMessage.value = "Failed to parse profile data"
                        }
                    } else {
                        _errorMessage.value = "Failed to fetch profile: ${response.errorBody()?.string()}"
                    }
                } else {
                    _errorMessage.value = "User not authenticated"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun updateUserExtras(
        contactNumber: String,
        address: String,
        birthdate: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token != null) {
                    val authHeader = "Bearer $token"
                    val request = UserExtraInfoRequest(
                        contactNumber = contactNumber,
                        address = address,
                        birthdate = birthdate
                    )
                    val response = apiService.updateUserExtras(authHeader, request)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.success == true) {
                            _userInfo.value = _userInfo.value.copy(
                                contactNumber = contactNumber,
                                address = address,
                                birthdate = birthdate
                            )
                            onSuccess()
                        } else {
                            onError(responseBody?.message ?: "Failed to update extras")
                        }
                    } else {
                        onError("Failed to update extras: ${response.errorBody()?.string()}")
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
