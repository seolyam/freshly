// UserViewModel.kt
package com.example.freshly.ui.theme

import androidx.lifecycle.ViewModel
import com.example.freshly.ui.theme.Constants.IS_DEVELOPMENT_MODE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class UserInfo(
    var firstName: String = "",
    var middleInitial: String = "",
    var lastName: String = "",
    var birthdate: String = "",
    var address: String = "",
    var email: String = "",
    var password: String = ""
)

class UserViewModel : ViewModel() {
    private val _userInfo = MutableStateFlow(
        if (IS_DEVELOPMENT_MODE) {
            UserInfo(
                firstName = "Dev",
                middleInitial = "D",
                lastName = "User",
                birthdate = "01/01/1990",
                address = "123 Developer Lane",
                email = "developer@example.com",
                password = "password123"
            )
        } else {
            UserInfo()
        }
    )
    val userInfo: StateFlow<UserInfo> get() = _userInfo

    fun updateUserInfo(updatedInfo: UserInfo) {
        _userInfo.value = updatedInfo
    }
}



