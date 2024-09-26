package com.example.freshly.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by cartViewModel.cartItems.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Your Cart",
            style = TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(cartItems) { item ->
                CartItemRow(item, cartViewModel)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCheckoutClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(50.dp), // Set a fixed height for the button
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff128819))
        ) {
            Text(
                text = "Proceed to Checkout",
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, cartViewModel: CartViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlaceholderImage(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                text = "₱${item.price * item.quantity}",
                style = TextStyle(fontSize = 16.sp, color = Color.Gray)
            )
        }

        // Quantity Controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray.copy(alpha = 0.1f))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = "Decrease Quantity",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        cartViewModel.decrementItemQuantity(item)
                    }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${item.quantity}",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Increase Quantity",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        cartViewModel.incrementItemQuantity(item)
                    }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Remove Item",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    cartViewModel.removeItem(item)
                }
        )
    }
}