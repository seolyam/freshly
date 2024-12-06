package com.example.freshly.ui.theme

import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPage(
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit,
    onPlaceOrder: () -> Unit,
    onEditInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalProductPrice = cartViewModel.getTotalPrice()
    val shippingFee = 15.0
    val totalPrice = totalProductPrice + shippingFee
    val userInfo by userViewModel.userInfo.collectAsState()

    val shippingDetails = "${userInfo.firstName} ${userInfo.middleInitial} ${userInfo.lastName}\n" +
            "${userInfo.contactNumber}\n" +
            "${userInfo.address}"

    Column(
        modifier = modifier.fillMaxSize().background(Color.White)
    ) {
        TopAppBar(
            title = { Text("CHECKOUT", fontSize = 14.sp, color = Color.Black) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.eparrowleftnotail),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Shipping Details:", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(shippingDetails, fontSize = 15.sp, color = Color(0xFF898989))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Edit Info",
                fontSize = 15.sp,
                color = Color(0xFF4E4E4E),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onEditInfoClick() }
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFC4C4C4))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cartItems) { item ->
                CheckoutCartItem(item)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFC4C4C4))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Payment Method:", fontSize = 15.sp, color = Color.Black)
                Text("Cash On Delivery", fontSize = 15.sp, color = Color(0xFF898989))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Total Products: ₱${"%.2f".format(totalProductPrice)}",
                fontSize = 15.sp,
                color = Color(0xFF898989)
            )
            Text(
                "Shipping Fee: ₱${"%.2f".format(shippingFee)}",
                fontSize = 15.sp,
                color = Color(0xFF898989)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", fontSize = 20.sp, color = Color.Black)
                Text("₱${"%.2f".format(totalPrice)}", fontSize = 20.sp, color = Color.Black)
            }
        }

        Button(
            onClick = onPlaceOrder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Place Order", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CheckoutCartItem(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!item.imageUrl.isNullOrEmpty()) {
            coil.compose.AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            PlaceholderImage(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.name,
                fontSize = 16.sp,
                color = Color(0xFF06161C),
                fontWeight = FontWeight.Medium
            )
            Text(
                "₱${"%.2f".format(item.price)} x ${item.quantity}",
                fontSize = 16.sp,
                color = Color(0xFFFF324B)
            )
        }
        Text(
            "x${item.quantity}",
            fontSize = 18.sp,
            color = Color(0xFF06161C),
            fontWeight = FontWeight.Medium
        )
    }
}
