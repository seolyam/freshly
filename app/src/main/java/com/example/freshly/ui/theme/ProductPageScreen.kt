package com.example.freshly.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.freshly.R
import kotlinx.coroutines.launch

@Composable
fun ProductPageScreen(
    productId: Int,
    productName: String,
    productPrice: Double,
    productDescription: String,
    productAllergens: String,
    productImageUrl: String?,
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
                    modifier = Modifier.padding(16.dp),
                    containerColor = Color.White,
                    contentColor = Color(0xFF201E1E),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Added to Cart", color = Color(0xFF201E1E))
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
                productImageUrl = productImageUrl,
                productAllergens = productAllergens,
                onAddToCart = { quantity ->
                    // Directly add to remote cart, no local add before remote.
                    cartViewModel.addItemToRemoteCart(productId, quantity,
                        onSuccess = {
                            showAddedToCartSnackbar()
                        },
                        onError = { /* Handle error if needed */ }
                    )
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
    onAddToCart: (Int) -> Unit,
    productImageUrl: String?,
    onNavigateBack: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quantity by rememberSaveable { mutableIntStateOf(1) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 24.dp)
    ) {
        // Top Bar with Back Arrow, Logo, and Cart Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.eparrowleftnotail),
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onNavigateBack() },
                tint = Color.Black
            )

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

            Icon(
                painter = painterResource(id = R.drawable.materialsymbolsshoppingcartoutline),
                contentDescription = "Cart",
                tint = Color(0xFF201E1E),
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onCartClick() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product Name
        Text(
            text = productName,
            color = Color(0xFF201E1E),
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Product Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(300.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        ) {
            if (productImageUrl != null && productImageUrl.isNotBlank()) {
                AsyncImage(
                    model = productImageUrl,
                    contentDescription = productName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = "No Image Available",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price and Quantity Section
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = "Quantity",
                    color = Color(0xFF201E1E),
                    style = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(30.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_minus),
                            contentDescription = "Decrease quantity",
                            tint = Color(0xFF201E1E),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "$quantity",
                        color = Color(0xFF201E1E),
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { quantity++ },
                        modifier = Modifier.size(30.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
                        contentPadding = PaddingValues(0.dp)
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

            Text(
                text = "₱${"%.2f".format(productPrice * quantity)}",
                color = Color(0xFF201E1E),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Description Section
        Text(
            text = "DESCRIPTION",
            color = Color(0xFF201E1E),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = productDescription,
            color = Color(0xFF201E1E),
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Allergens Section
        Text(
            text = "ALLERGENS",
            color = Color(0xFF201E1E),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = productAllergens,
            color = Color(0xFF201E1E),
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onAddToCart(quantity) },
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
