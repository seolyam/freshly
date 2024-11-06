// LoginScreen.kt
package com.example.freshly.ui.theme

// Import TextFieldWithError from TextFieldWithError.kt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R

@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    // Observing error messages from the ViewModel
    val errorMessage by userViewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Freshly()
        Spacer(modifier = Modifier.height(16.dp))

        // Login Fields
        LoginFields(
            username = username,
            onUsernameChange = {
                username = it
                usernameError = "" // Clear error when typing
            },
            usernameError = usernameError,
            password = password,
            onPasswordChange = {
                password = it
                passwordError = "" // Clear error when typing
            },
            passwordError = passwordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        LoginButton(onClick = {
            var hasError = false

            // Reset error messages
            usernameError = ""
            passwordError = ""

            // Validate username
            if (username.isEmpty()) {
                usernameError = "Username is required"
                hasError = true
            }

            // Validate password
            if (password.isEmpty()) {
                passwordError = "Password is required"
                hasError = true
            }

            if (!hasError) {
                // Call login function from ViewModel
                userViewModel.login(username, password) {
                    onLoginSuccess()
                }
            }
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Display error message from ViewModel
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        OrLogInWith()
        Spacer(modifier = Modifier.height(16.dp))
        SocialLogInButtons()
    }
}

@Composable
fun Freshly(modifier: Modifier = Modifier) {
    Text(
        textAlign = TextAlign.Center,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF201E1E),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
            ) { append("Log in to ") }

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
fun LoginFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    usernameError: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .requiredWidth(326.dp)
    ) {
        // Username Field
        TextFieldWithError(
            label = "Username",
            value = username,
            onValueChange = onUsernameChange,
            error = usernameError
        )

        // Password Field
        TextFieldWithError(
            label = "Password",
            value = password,
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            error = passwordError
        )
    }
}

@Composable
fun LoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .requiredWidth(326.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xff128819))
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Log In",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OrLogInWith(modifier: Modifier = Modifier) {
    Text(
        text = "or Log in with",
        color = Color(0xff141414),
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun SocialLogInButtons(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .requiredWidth(325.dp)
    ) {
        SocialLogInButton(
            imageResource = R.drawable.logosfacebook,
            buttonText = "Log In with Facebook"
        )
        SocialLogInButton(
            imageResource = R.drawable.flatcoloriconsgoogle,
            buttonText = "Log In with Google"
        )
    }
}

@Composable
fun SocialLogInButton(
    imageResource: Int,
    buttonText: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xff141414),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { /* Handle social login */ }
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = buttonText,
            color = Color(0xff141414),
            fontSize = 14.sp
        )
    }
}
