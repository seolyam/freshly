package com.example.freshly.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp),
                    containerColor = Color.White,  // Set background color to white
                    contentColor = Color(0xFF201E1E),  // Set text color to #201E1E
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Added to Cart", color = Color(0xFF201E1E))  // Change text color
                }
            }
        }
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
                onAddToCart = { quantity ->
                    val item = CartItem(
                        name = productName,
                        quantity = quantity, // Use selected quantity
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
    onAddToCart: (Int) -> Unit, // Accepts quantity
    onNavigateBack: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Add quantity state variable
    var quantity by remember { mutableIntStateOf(1) }

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
                painter = painterResource(id = R.drawable.eparrowleftnotail),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // Use Color.Unspecified to retain the original colors of the PNG
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
                tint = Color(0xFF201E1E),  // Replace black with #201E1E
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onCartClick() }  // Navigate to cart
            )
        }

        Spacer(modifier = Modifier.height(16.dp))  // Adds space before the product name

        // Product Name at the top
        Text(
            text = productName,
            color = Color(0xFF201E1E),  // Replace black with #201E1E
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp) // Adjust spacing below the product name
        )

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

        // Price and Quantity Section
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically, // Center items vertically
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Quantity Column
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Quantity",
                    color = Color(0xFF201E1E),  // Replace black with #201E1E
                    style = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))  // Add space between label and controls

                // Quantity Controls: Decrease, Quantity, Increase
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus Button
                    Button(
                        onClick = { if (quantity > 1) quantity-- }, // Decrease quantity but not below 1
                        modifier = Modifier.size(30.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        contentPadding = PaddingValues(0.dp) // Remove default padding
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_minus),
                            contentDescription = "Decrease quantity",
                            tint = Color(0xFF201E1E),  // Replace black with #201E1E
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Display the current quantity
                    Text(
                        text = "$quantity",
                        color = Color(0xFF201E1E),  // Replace black with #201E1E
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Plus Button
                    Button(
                        onClick = { quantity++ }, // Increase quantity
                        modifier = Modifier.size(30.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
                        contentPadding = PaddingValues(0.dp) // Remove default padding
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Increase quantity",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Price Column
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Price",
                    color = Color(0xFF201E1E),  // Replace black with #201E1E
                    style = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))  // Space between label and price

                // Price Value
                Text(
                    text = "₱${"%.2f".format(productPrice * quantity)}", // Multiply by quantity
                    color = Color(0xFF201E1E),  // Replace black with #201E1E
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Description Section
        Text(
            text = "DESCRIPTION",
            color = Color(0xFF201E1E),  // Replace black with #201E1E
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = productDescription,  // Display dynamic description
            color = Color(0xFF201E1E),  // Replace black with #201E1E
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        // Allergens Section
        Text(
            text = "ALLERGENS",
            color = Color(0xFF201E1E),  // Replace black with #201E1E
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = productAllergens,  // Display dynamic allergens
            color = Color(0xFF201E1E),  // Replace black with #201E1E
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Add to Cart Button
        Button(
            onClick = { onAddToCart(quantity) }, // Pass quantity to onAddToCart
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Add To Cart",
                color = Color.White,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}
