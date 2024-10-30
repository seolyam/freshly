// InfoPage.kt
package com.example.freshly.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R
import java.util.Calendar

@Composable
fun InfoPage(
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit,
    onSignUpComplete: () -> Unit
) {
    // State variables for user inputs
    var firstName by remember { mutableStateOf("") }
    var middleInitial by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

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

    // Define your custom colors
    val primaryColor = Color(0xFF201E1E) // Your black font color
    val placeholderColor = Color(0xFFA8A8A8) // Gray placeholder color
    val borderColor = Color(0xFF201E1E) // Border color for text fields

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center the fields
    ) {
        // Top bar with back arrow
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.eparrowleftnotail),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified // Use Color.Unspecified to retain the original colors of the PNG
                )
            }
        }

        // Sign-up to Freshly Title
        SignupToFreshly(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Personal Information Section Title
        Text(
            text = "Personal Information",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // First Name and Middle Initial
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // First Name Field
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "First Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                CustomBasicTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "Ex. Juan",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Middle Initial Field
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Middle Initial",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                CustomBasicTextField(
                    value = middleInitial,
                    onValueChange = { middleInitial = it },
                    placeholder = "Ex. J",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Last Name and Birthdate
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Last Name Field
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Last Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                CustomBasicTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "Ex. Dela Cruz",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Birthdate Field
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Birthdate",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                CustomBasicTextField(
                    value = birthdate,
                    onValueChange = { /* Read-only */ },
                    placeholder = "MM/DD/YYYY",
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                datePickerDialog.show()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = "Select Date",
                                tint = primaryColor
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Address Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Address",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            CustomBasicTextField(
                value = address,
                onValueChange = { address = it },
                placeholder = "Ex. 1234 Barangay ABC, Bacolod City",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = {
                userViewModel.updateUserProfile(
                    firstName = firstName,
                    middleInitial = middleInitial,
                    lastName = lastName,
                    birthdate = birthdate,
                    address = address,
                    onSuccess = {
                        // Only navigate if update succeeds
                        onSignUpComplete()
                    },
                    onError = { errorMessage ->
                        // Show an error message if update fails
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun SignupToFreshly(modifier: Modifier = Modifier) {
    Text(
        textAlign = TextAlign.Center,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(0xff201e1e),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            ) { append("Sign-up to") }
            withStyle(
                style = SpanStyle(
                    color = Color(0xff201e1e),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            ) { append(" ") }
            withStyle(
                style = SpanStyle(
                    color = Color(0xff128819),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            ) { append("Fresh") }
            withStyle(
                style = SpanStyle(
                    color = Color(0xff6fb103),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            ) { append("ly") }
        },
        modifier = modifier
    )
}

@Composable
fun CustomBasicTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null

) {
    val primaryColor = Color(0xFF201E1E)
    val placeholderColor = Color(0xFFA8A8A8)
    val borderColor = Color(0xFF201E1E)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        textStyle = TextStyle(
            color = primaryColor,
            fontSize = 14.sp
        ),
        cursorBrush = SolidColor(primaryColor),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .height(48.dp) // Set fixed height
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize() // Fill the height and width
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = placeholderColor,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                    if (trailingIcon != null) {
                        Box(
                            modifier = Modifier.size(24.dp)
                        ) {
                            trailingIcon()
                        }
                    }
                }
            }
        },
        modifier = modifier
    )
}
