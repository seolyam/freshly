package com.example.freshly.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Phone(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,   // Accept onLoginClick parameter
    onSignUpClick: () -> Unit   // Accept onSignUpClick parameter
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color(0xff128819),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold)) { append("Fresh") }
                withStyle(style = SpanStyle(
                    color = Color(0xff6fb103),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold)) { append("ly") }
            },
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(horizontal = 16.dp)
        )

        // Login Button
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 20.dp)
                .fillMaxWidth(0.45f)
                .height(50.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .clickable { onLoginClick() } // Use the parameter here
                .padding(10.dp)
        ) {
            Text(
                text = "LOGIN",
                color = Color(0xFF201E1E),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
            )
        }

        // Sign-up Button
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 20.dp)
                .fillMaxWidth(0.45f)
                .height(50.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color(0xff128819))
                .clickable { onSignUpClick() } // Use the parameter here
                .padding(10.dp)
        ) {
            Text(
                text = "SIGN-UP",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}
