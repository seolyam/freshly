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
    val userInfo by userViewModel.userInfo.collectAsState()

    var contactNumber by remember { mutableStateOf(userInfo.contactNumber) }
    var address by remember { mutableStateOf(userInfo.address) }
    var birthdate by remember { mutableStateOf(userInfo.birthdate) }
    var updatedPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val context = LocalContext.current

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

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = R.drawable.eparrowleftnotail),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Edit Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(24.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EditableField(
                label = "Contact Number",
                value = contactNumber,
                onValueChange = { contactNumber = it }
            )

            EditableField(
                label = "Address",
                value = address,
                onValueChange = { address = it }
            )

            EditableField(
                label = "Birthdate",
                value = birthdate,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Select Date"
                        )
                    }
                }
            )

            EditableField(
                label = "Update Password",
                value = updatedPassword,
                onValueChange = { updatedPassword = it },
                isPassword = true
            )

            EditableField(
                label = "Confirm Password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                isPassword = true
            )

            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (updatedPassword == confirmPassword) {
                        showConfirmationDialog = true
                    } else {
                        passwordError = "Passwords do not match"
                    }
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
        }

        if (showConfirmationDialog) {
            CustomDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = "Confirm Changes",
                message = "Are you sure you want to save these changes?",
                confirmButtonText = "Confirm",
                onConfirm = {
                    showConfirmationDialog = false
                    userViewModel.updateUserExtras(
                        contactNumber = contactNumber,
                        address = address,
                        birthdate = birthdate,
                        onSuccess = {
                            showSuccessDialog = true
                        },
                        onError = { error ->
                            errorMessage = error
                            showErrorDialog = true
                        }
                    )
                },
                dismissButtonText = "Cancel",
                onDismiss = { showConfirmationDialog = false }
            )
        }

        if (showSuccessDialog) {
            CustomDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = "Success",
                message = "Your changes have been saved successfully.",
                confirmButtonText = "OK",
                onConfirm = {
                    showSuccessDialog = false
                    onSave()
                }
            )
        }

        if (showErrorDialog) {
            CustomDialog(
                onDismissRequest = { showErrorDialog = false },
                title = "Error",
                message = errorMessage,
                confirmButtonText = "OK",
                onConfirm = {
                    showErrorDialog = false
                }
            )
        }
    }
}

@Composable
fun EditableField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
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
                .height(44.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    readOnly = readOnly,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = if (readOnly) Color.Gray else Color.Black
                    ),
                    visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                    modifier = Modifier.weight(1f)
                )
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                        )
                    }
                }
                trailingIcon?.invoke()
            }
        }
    }
}

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
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = dismissButtonText,
                                color = Color.White,
                                fontSize = 14.sp
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
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

}