// SignUpScreen.kt
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
fun SignUpScreen(
    userViewModel: UserViewModel,
    onSignUpSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State variables for user inputs
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // State variables for error messages
    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    // Observe error message from ViewModel
    val errorMessage by userViewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FreshlySignUp()
        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Fields
        SignUpFields(
            username = username,
            onUsernameChange = {
                username = it
                usernameError = "" // Clear error when user starts typing
            },
            usernameError = usernameError,
            email = email,
            onEmailChange = {
                email = it
                emailError = "" // Clear error when user starts typing
            },
            emailError = emailError,
            password = password,
            onPasswordChange = {
                password = it
                passwordError = "" // Clear error when user starts typing
            },
            passwordError = passwordError,
            confirmPassword = confirmPassword,
            onConfirmPasswordChange = {
                confirmPassword = it
                confirmPasswordError = "" // Clear error when user starts typing
            },
            confirmPasswordError = confirmPasswordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Button
        SignUpButton(onClick = {
            var hasError = false

            // Reset error messages
            usernameError = ""
            emailError = ""
            passwordError = ""
            confirmPasswordError = ""

            // Validate username
            if (username.isEmpty()) {
                usernameError = "Username is required"
                hasError = true
            }

            // Validate email
            if (email.isEmpty()) {
                emailError = "Email is required"
                hasError = true
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError = "Invalid email address"
                hasError = true
            }

            // Validate password
            if (password.isEmpty()) {
                passwordError = "Password is required"
                hasError = true
            } else if (password.length < 6) {
                passwordError = "Password must be at least 6 characters"
                hasError = true
            }

            // Validate confirm password
            if (confirmPassword.isEmpty()) {
                confirmPasswordError = "Please confirm your password"
                hasError = true
            } else if (password != confirmPassword) {
                confirmPasswordError = "Passwords do not match"
                hasError = true
            }

            if (!hasError) {
                // Call register function from ViewModel
                userViewModel.register(
                    username, email, password,
                    onSuccess = { onSignUpSuccess() },
                    onError = { message -> usernameError = message }
                )
            }
        })

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
        OrSignUpWith()
        Spacer(modifier = Modifier.height(16.dp))
        SocialSignUpButtons()
    }
}

@Composable
fun FreshlySignUp(modifier: Modifier = Modifier) {
    Text(
        textAlign = TextAlign.Center,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF201E1E),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
            ) { append("Sign Up to ") }

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
fun SignUpFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    usernameError: String,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordError: String,
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
        // Email Field
        TextFieldWithError(
            label = "Email",
            value = email,
            onValueChange = onEmailChange,
            error = emailError
        )
        // Password Field
        TextFieldWithError(
            label = "Password",
            value = password,
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            error = passwordError
        )
        // Confirm Password Field
        TextFieldWithError(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            error = confirmPasswordError
        )
    }
}

@Composable
fun SignUpButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
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
            text = "Sign Up",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OrSignUpWith(modifier: Modifier = Modifier) {
    Text(
        text = "or Sign Up with",
        color = Color(0xff141414),
        fontSize = 14.sp,
        modifier = modifier
    )
}

@Composable
fun SocialSignUpButtons(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .requiredWidth(325.dp)
    ) {
        SocialSignUpButton(
            imageResource = R.drawable.logosfacebook,
            buttonText = "Sign Up with Facebook"
        )
        SocialSignUpButton(
            imageResource = R.drawable.flatcoloriconsgoogle,
            buttonText = "Sign Up with Google"
        )
    }
}

@Composable
fun SocialSignUpButton(
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
            .clickable { /* Handle social sign up */ }
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
