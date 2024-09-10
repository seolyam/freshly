package com.example.freshly.ui.theme

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, modifier: Modifier = Modifier) {
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
        LoginFields()
        Spacer(modifier = Modifier.height(16.dp))
        LoginButton(onClick = onLoginSuccess)
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
                    color = Color.Black,
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
fun LoginFields(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
        modifier = modifier
            .requiredWidth(326.dp)
    ) {
        Text(
            text = "Email",
            color = Color(0xff141414),
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        BasicTextField(
            value = email,
            onValueChange = { email = it },
            textStyle = TextStyle(color = Color(0xff141414)),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    BorderStroke(1.dp, Color(0xff141414)),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .background(Color.White)
        )

        Text(
            text = "Password",
            color = Color(0xff141414),
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        BasicTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            textStyle = TextStyle(color = Color(0xff141414)),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    BorderStroke(1.dp, Color(0xff141414)),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .background(Color.White)
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
            .padding(horizontal = 68.dp, vertical = 10.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = "Log In",
            color = Color.White,
            style = TextStyle(
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun OrLogInWith(modifier: Modifier = Modifier) {
    Text(
        text = "or Log-in with",
        color = Color(0xff141414),
        style = TextStyle(
            fontSize = 14.sp
        ),
        modifier = modifier
    )
}

@Composable
fun SocialLogInButtons(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(21.dp, Alignment.Top),
        modifier = modifier
            .requiredWidth(325.dp)
    ) {
        SocialLogInButton(
            imageResource = R.drawable.logosfacebook,
            buttonText = "Log In With Facebook"
        )
        SocialLogInButton(
            imageResource = R.drawable.flatcoloriconsgoogle,
            buttonText = "Log In With Google"
        )
    }
}

@Composable
fun SocialLogInButton(
    imageResource: Int,
    buttonText: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(41.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                BorderStroke(1.dp, Color(0xff141414)),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 13.dp, vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = buttonText,
                color = Color(0xff141414),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {})
}
