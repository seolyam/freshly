// UserViewModel.kt
package com.example.freshly.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UserInfo(
    var firstName: String = "",
    var middleInitial: String = "",
    var lastName: String = "",
    var birthdate: String = "",
    var address: String = ""
)

class UserViewModel : ViewModel() {
    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> get() = _userInfo

    fun updateUserInfo(updatedInfo: UserInfo) {
        _userInfo.value = updatedInfo
    }
}
