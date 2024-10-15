// EditProfileScreen.kt
package com.example.freshly.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Observe the userInfo from UserViewModel
    val userInfo by userViewModel.userInfo.collectAsState()

    // State variables for user inputs
    var firstName by remember { mutableStateOf(userInfo.firstName) }
    var middleInitial by remember { mutableStateOf(userInfo.middleInitial) }
    var lastName by remember { mutableStateOf(userInfo.lastName) }
    var birthdate by remember { mutableStateOf(userInfo.birthdate) }
    var address by remember { mutableStateOf(userInfo.address) }
    var email by remember { mutableStateOf(userInfo.email) }
    var password by remember { mutableStateOf(userInfo.password) }

    val context = LocalContext.current

    // Date Picker Dialog
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                birthdate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
            },
            year,
            month,
            day
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top bar with back arrow, "User Profile" text, and home button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF141414)
                )
            }
            Text(
                text = "User Profile",
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Navigate to Home */ }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color(0xFF141414),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            // Profile Icon centered and larger
            Box(
                modifier = Modifier
                    .size(100.dp) // Increase the size of the icon
                    .clip(CircleShape)
                    .background(Color(0xFF128819)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Image",
                    tint = Color.White,
                    modifier = Modifier.size(80.dp) // Adjust icon size inside the box
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Add space between icon and content

            // Personal Information Section
            Text(
                text = "Personal Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF201E1E),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start) // Align text to the start
            )

            // First Name and Middle Initial
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EditableField(
                    label = "First Name",
                    value = firstName,
                    onValueChange = { firstName = it },
                    modifier = Modifier.weight(1f)
                )
                EditableField(
                    label = "Middle Initial",
                    value = middleInitial,
                    onValueChange = { middleInitial = it },
                    modifier = Modifier.weight(1f)
                )
            }

            // Last Name and Birthdate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EditableField(
                    label = "Last Name",
                    value = lastName,
                    onValueChange = { lastName = it },
                    modifier = Modifier.weight(1f)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Birthdate",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFCBC6C6)),
                        color = Color(0xFFF8F8F8),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable { datePickerDialog.show() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = birthdate,
                                color = Color(0xFF141414),
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Select Date",
                                tint = Color(0xFF141414)
                            )
                        }
                    }
                }
            }

            // Address
            EditableField(
                label = "Address",
                value = address,
                onValueChange = { address = it }
            )

            // Email
            EditableField(
                label = "Email",
                value = email,
                onValueChange = { email = it }
            )

            // Password
            EditableField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )
        }

        // Buttons at the bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Save Changes Button
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF128819))
                    .clickable {
                        // Update UserViewModel with the collected information
                        val updatedUserInfo = UserInfo(
                            firstName = firstName,
                            middleInitial = middleInitial,
                            lastName = lastName,
                            birthdate = birthdate,
                            address = address,
                            email = email,
                            password = password
                        )
                        userViewModel.updateUserInfo(updatedUserInfo)
                        onSave()
                    }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save Changes",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Log out Button
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF8B0000))
                    .clickable { onLogout() }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Log out",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EditableField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFCBC6C6)),
            color = Color(0xFFF8F8F8),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                textStyle = TextStyle(
                    color = Color(0xFF141414),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        innerTextField()
                    }
                }
            )
        }
    }
}
