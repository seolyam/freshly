package com.example.freshly.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
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
fun SignUpScreen(modifier: Modifier = Modifier) {
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
        CreateAnAccount()
        Spacer(modifier = Modifier.height(16.dp))
        Frame8()
        Spacer(modifier = Modifier.height(16.dp))
        Frame9()
        Spacer(modifier = Modifier.height(16.dp))
        OrSignupWith()
        Spacer(modifier = Modifier.height(16.dp))
        Frame14()
    }
}

@Composable
fun FreshlySignUp(modifier: Modifier = Modifier) {
    Text(
        textAlign = TextAlign.Center,
        text = buildAnnotatedString {
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
fun CreateAnAccount(modifier: Modifier = Modifier) {
    Text(
        text = "Create an account",
        color = Color(0xff232222),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
    )
}


@Composable
fun Frame8(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
        modifier = modifier
            .requiredWidth(width = 326.dp)
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
                .clip(shape = RoundedCornerShape(8.dp))
                .border(
                    border = BorderStroke(1.dp, Color(0xff141414)),
                    shape = RoundedCornerShape(8.dp)
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
            textStyle = TextStyle(color = Color(0xff141414)), // Text color inside the field
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .border(
                    border = BorderStroke(1.dp, Color(0xff141414)),
                    shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .background(Color.White) // Background color of the field
        )
    }
}




@Composable
fun Frame9(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .requiredWidth(width = 325.dp)
            .requiredHeight(height = 37.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Color(0xff128819))
            .padding(horizontal = 68.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Sign Up",
            color = Color.White,
            style = TextStyle(
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun OrSignupWith(modifier: Modifier = Modifier) {
    Text(
        text = "or sign-up with",
        color = Color(0xff141414),
        style = TextStyle(
            fontSize = 14.sp
        ),
        modifier = modifier
    )
}

@Composable
fun Frame14(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(21.dp, Alignment.Top),
        modifier = modifier
            .requiredWidth(width = 325.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = 41.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .border(
                    border = BorderStroke(1.dp, Color(0xff141414)),
                    shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 13.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(62.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logosfacebook),
                    contentDescription = "logos:facebook",
                    modifier = Modifier
                        .requiredSize(size = 20.dp)
                )
                Text(
                    text = "Sign Up With Facebook",
                    color = Color(0xff141414),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = 41.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .border(
                    border = BorderStroke(1.dp, Color(0xff141414)),
                    shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 13.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(56.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .requiredWidth(width = 298.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.flatcoloriconsgoogle),
                    contentDescription = "flat-color-icons:google",
                    modifier = Modifier
                        .requiredSize(size = 24.dp)
                )
                Text(
                    text = "Sign Up With Google",
                    color = Color(0xff141414),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}

@Preview
@Composable
private fun Frame8Preview() {
    Frame8(Modifier)
}
