// EditProfileScreen.kt
package com.example.freshly.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R
import java.util.Calendar

@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Observe the userInfo from UserViewModel
    val userInfo by userViewModel.userInfo.collectAsState()

    // State variables for user inputs
    var firstName by remember { mutableStateOf(userInfo.firstName) }
    var middleInitial by remember { mutableStateOf(userInfo.middleInitial) }
    var lastName by remember { mutableStateOf(userInfo.lastName) }
    var email by remember { mutableStateOf(userInfo.email) } // Added email
    var birthdate by remember { mutableStateOf(userInfo.birthdate) }
    var address by remember { mutableStateOf(userInfo.address) }
    var updatedPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

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

    // State for showing custom dialogs
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Content with scroll capability
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Spacer for top padding
            Spacer(modifier = Modifier.height(12.dp))

            // Top bar with back arrow and "Edit Profile" text centered
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                // Back arrow aligned to the start
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.eparrowleftnotail),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }

                // "Edit Profile" text centered
                Text(
                    text = "Edit Profile",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Spacer between top bar and content
            Spacer(modifier = Modifier.height(8.dp))

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Icon centered
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(Color(0xFF128819), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile Image",
                        tint = Color.White,
                        modifier = Modifier.size(128.dp)
                    )
                }

                // Spacer between profile icon and content
                Spacer(modifier = Modifier.height(16.dp))

                // Personal Information Section
                Text(
                    text = "Personal Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF201E1E),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.Start)
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

                // Email Field
                EditableField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth()
                )

                // Address
                EditableField(
                    label = "Address",
                    value = address,
                    onValueChange = { address = it }
                )

                // Update Password
                EditableField(
                    label = "Update Password",
                    value = updatedPassword,
                    onValueChange = { updatedPassword = it },
                    isPassword = true
                )

                // Confirm Password
                EditableField(
                    label = "Confirm Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    isPassword = true
                )

                // Reserve space for error message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (passwordError.isNotEmpty()) {
                        Text(
                            text = passwordError,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }

                // Save Changes Button
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        showConfirmationDialog = true
                        passwordError = "" // Clear previous error
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Save Changes",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Spacer at the bottom to prevent content from being cut off
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Custom Confirmation Dialog
        if (showConfirmationDialog) {
            CustomDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = "Confirm Changes",
                message = "Are you sure you want to save these changes?",
                confirmButtonText = "Confirm",
                onConfirm = {
                    showConfirmationDialog = false
                    // Handle save action
                    if (updatedPassword == confirmPassword) {
                        userViewModel.updateUserProfile(
                            firstName = firstName,
                            middleInitial = middleInitial,
                            lastName = lastName,
                            email = email, // Pass email here
                            birthdate = birthdate,
                            address = address,
                            password = if (updatedPassword.isNotEmpty()) updatedPassword else null,
                            onSuccess = {
                                showSuccessDialog = true
                            },
                            onError = { errorMsg ->
                                errorMessage = errorMsg
                                showErrorDialog = true
                            }
                        )
                    } else {
                        passwordError = "Passwords do not match"
                        showErrorDialog = true
                    }
                },
                dismissButtonText = "Cancel",
                onDismiss = { showConfirmationDialog = false }
            )
        }

        // Custom Success Dialog
        if (showSuccessDialog) {
            CustomDialog(
                onDismissRequest = { /* Do nothing */ },
                title = "Success",
                message = "Your changes have been saved successfully.",
                confirmButtonText = "OK",
                onConfirm = {
                    showSuccessDialog = false
                    onSave()
                }
            )
        }

        // Custom Error Dialog
        if (showErrorDialog) {
            CustomDialog(
                onDismissRequest = { showErrorDialog = false },
                title = "Error",
                message = errorMessage, // Display the error message
                confirmButtonText = "OK",
                onConfirm = {
                    showErrorDialog = false
                }
            )
        }
    }
}

// ... [Include CustomDialog and EditableField composables as in your original code]



@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    dismissButtonText: String? = null,
    onDismiss: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismissRequest)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .clickable(enabled = false) { }
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF201E1E)
                )
                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = Color(0xFF141414)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (dismissButtonText != null) Arrangement.SpaceBetween else Arrangement.End
                ) {
                    if (dismissButtonText != null && onDismiss != null) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAAAAAA)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = dismissButtonText,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = confirmButtonText,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditableField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false

) {
    var passwordVisible by remember { mutableStateOf(false) }
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
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                textStyle = TextStyle(
                    color = Color(0xFF141414),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            innerTextField()
                        }
                        if (isPassword) {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.size(24.dp)
                            ) {
                                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                                val description = if (passwordVisible) "Hide password" else "Show password"
                                Icon(
                                    imageVector = icon,
                                    contentDescription = description,
                                    tint = Color(0xFF141414)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
