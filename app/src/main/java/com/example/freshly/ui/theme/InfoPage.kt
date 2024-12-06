package com.example.freshly.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R

@Composable
fun InfoPage(
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit,
    onSignUpComplete: () -> Unit
) {
    val userInfo by userViewModel.userInfo.collectAsState()

    // Remove birthdate variable entirely since we no longer need it
    var contactNumber by remember { mutableStateOf(userInfo.contactNumber) }
    var address by remember { mutableStateOf(userInfo.address) }

    val context = LocalContext.current
    val primaryColor = Color(0xFF201E1E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar with Back Arrow
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.eparrowleftnotail),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Update Shipping Info",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Display user's name
        // Fix the retrieval: ensure that fetchUserProfile sets userInfo.firstName and lastName properly
        Text(
            text = "Name: ${userInfo.firstName} ${userInfo.lastName}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Contact Number Field
        Text(
            text = "Contact Number",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        CustomBasicTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            placeholder = "Ex. 09123456789",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Address Field
        Text(
            text = "Address",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        CustomBasicTextField(
            value = address,
            onValueChange = { address = it },
            placeholder = "Ex. 1234 Barangay ABC, City",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Update Button (Only updates extras now)
        Button(
            onClick = {
                if (contactNumber.isEmpty() || address.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please provide contact number and address.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // Directly call updateUserExtras only (no birthdate)
                    userViewModel.updateUserExtras(
                        contactNumber = contactNumber,
                        address = address,
                        birthdate = userInfo.birthdate, // keep the stored birthdate or empty if desired
                        onSuccess = {
                            onSignUpComplete()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Update Info",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
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
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
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
