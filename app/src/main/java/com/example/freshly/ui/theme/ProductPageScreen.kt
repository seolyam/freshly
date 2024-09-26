package com.example.freshly.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.R
import kotlinx.coroutines.launch

@Composable
fun ProductPageScreen(
    productName: String,
    productPrice: Double,
    productDescription: String,
    productAllergens: String,
    onNavigateBack: () -> Unit,
    onCartClick: () -> Unit,
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showAddedToCartSnackbar() {
        scope.launch {
            snackbarHostState.showSnackbar("Added to Cart")
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Color.White)
        ) {
            ProductPage(
                productName = productName,
                productPrice = productPrice,
                productDescription = productDescription,
                productAllergens = productAllergens,
                onAddToCart = {
                    val item = CartItem(
                        name = productName,
                        quantity = 1,
                        price = productPrice,
                    )
                    cartViewModel.addItem(item)
                    showAddedToCartSnackbar()
                },
                onNavigateBack = onNavigateBack,
                onCartClick = onCartClick
            )
        }
    }
}

@Composable
fun ProductPage(
    productName: String,
    productPrice: Double,
    productDescription: String,
    productAllergens: String,
    onAddToCart: () -> Unit,
    onNavigateBack: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 24.dp)
    ) {
        // Top Bar with Back Arrow and Logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Arrow with navigation function
            Icon(
                painter = painterResource(id = R.drawable.eparrowleft),
                contentDescription = "Back",
                tint = Color(0xFF141414),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNavigateBack() }  // Navigate back to homepage
            )
            // Logo
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF128819), fontSize = 18.sp, fontWeight = FontWeight.Bold)) {
                        append("Fresh")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF6FB103), fontSize = 18.sp, fontWeight = FontWeight.Bold)) {
                        append("ly")
                    }
                },
                textAlign = TextAlign.Center
            )
            // Shopping Cart Icon with navigation function
            Icon(
                painter = painterResource(id = R.drawable.materialsymbolsshoppingcartoutline),
                contentDescription = "Cart",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onCartClick() }  // Navigate to cart
            )
        }

        // Product Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(300.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray) // Placeholder image
                .padding(vertical = 16.dp)
        ) {
            PlaceholderImage(modifier = Modifier.fillMaxSize()) // Placeholder image composable
        }

        // Product Name
        Text(
            text = productName,
            color = Color(0xFF141414),
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Price and Quantity Section
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Quantity",
                color = Color(0xFF141414),
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = "Price ₱${"%.2f".format(productPrice)}",
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )
        }

        // Description Section
        Text(
            text = "DESCRIPTION",
            color = Color(0xFF141414),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = productDescription,  // Display dynamic description
            color = Color(0xFF141414),
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Allergens Section
        Text(
            text = "ALLERGENS",
            color = Color(0xFF141414),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = productAllergens,  // Display dynamic allergens
            color = Color(0xFF141414),
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Add to Cart Button
        Button(
            onClick = onAddToCart,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp)
        ) {
            Text(
                text = "ADD TO CART",
                color = Color.White,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}
