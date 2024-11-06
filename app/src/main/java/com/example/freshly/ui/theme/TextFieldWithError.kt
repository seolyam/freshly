// TextFieldWithError.kt
package com.example.freshly.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldWithError(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        Text(
            text = label,
            color = Color(0xff141414),
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.fillMaxWidth()
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            textStyle = TextStyle(color = Color(0xff141414)),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    BorderStroke(1.dp, if (error.isEmpty()) Color(0xff141414) else Color.Red),
                    RoundedCornerShape(8.dp)
                )
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
