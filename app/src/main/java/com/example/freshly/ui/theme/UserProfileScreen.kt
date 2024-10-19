// UserProfileScreen.kt
package com.example.freshly.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R

@Composable
fun UserProfileScreen(
    userViewModel: UserViewModel,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Observe the userInfo from UserViewModel
    val userInfo by userViewModel.userInfo.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        // Top bar with back arrow, "User Profile" text centered, and home icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp) // Adjusted padding
        ) {
            // Back arrow aligned to the start
            IconButton(
                onClick = { /* Handle back navigation if needed */ },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.eparrowleftnotail),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified // Retain the original color of the icon
                )
            }

            // "User Profile" text centered
            Text(
                text = "User Profile",
                color = Color.Black,
                fontSize = 20.sp, // Reduced font size
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )

            // Home icon aligned to the end
            IconButton(
                onClick = { /* Handle home navigation */ },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.homeicon),
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified // Retain the original color of the icon
                )
            }
        }

        // Moved profile icon up by reducing the top padding
        Spacer(modifier = Modifier.height(16.dp)) // Reduced height

        // Profile Icon centered
        Box(
            modifier = Modifier
                .size(100.dp) // Reduced size from 100.dp
                .align(Alignment.CenterHorizontally)
                .background(Color(0xFF128819), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Image",
                tint = Color.White,
                modifier = Modifier.size(128.dp) // Adjusted icon size
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Reduced height

        // Display user information in greyed-out fields
        DisplayField(
            label = "Name",
            value = "${userInfo.firstName} ${userInfo.middleInitial}. ${userInfo.lastName}"
        )
        DisplayField(label = "Email", value = userInfo.email)
        DisplayField(label = "Password", value = "**********")
        DisplayField(label = "Date of Birth", value = userInfo.birthdate)
        DisplayField(label = "Address", value = userInfo.address)

        Spacer(modifier = Modifier.height(64.dp)) // Reduced height

        // Edit Profile and Logout Buttons
        Button(
            onClick = onEditProfile,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Reduced height
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Edit Profile",
                color = Color.White,
                fontSize = 14.sp, // Reduced font size
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp)) // Reduced height

        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Reduced height
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Logout",
                color = Color.White,
                fontSize = 14.sp, // Reduced font size
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DisplayField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp) // Reduced vertical padding
    ) {
        Text(
            text = label,
            color = Color(0xFF201E1E),
            fontSize = 13.sp, // Reduced font size
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 2.dp) // Reduced bottom padding
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp) // Reduced height
                .background(Color(0xFFF8F8F8), shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFCBC6C6), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp) // Adjusted vertical padding
        ) {
            Text(
                text = value,
                color = Color(0xFFA7A2A2),
                fontSize = 13.sp // Reduced font size
            )
        }
    }
}
