package com.example.freshly.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomePageScreen(
    modifier: Modifier = Modifier,
    onCartClick: () -> Unit,
    onProductClick: (String, String, String) -> Unit // Correctly added onProductClick parameter
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(onCartClick)
        SearchSection(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color(0xff141414)), RoundedCornerShape(10.dp))
                .background(Color.White)
                .clip(RoundedCornerShape(10.dp))
        )
        ProductImageSection()
        ProductCategoriesSection(onProductClick)
        Spacer(modifier = Modifier.weight(1f)) // Pushes bottom navigation to the bottom
        BottomNavigationBar()
    }
}

@Composable
fun TopBar(onCartClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Home, // Placeholder for a hamburger icon
            contentDescription = "Menu Icon"
        )
        Text(text = "Freshly", color = Color(0xff4CAF50), style = TextStyle(fontSize = 24.sp))
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = "Cart Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onCartClick() } // Navigates to CartScreen
        )
    }
}

@Composable
fun SearchSection(modifier: Modifier) {
    Box(
        modifier = modifier
            .height(40.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Search an item",
                color = Color(0xff807d7d),
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}

@Composable
fun ProductImageSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray) // Replace with Image
    ) {
        // Placeholder for the large image
        PlaceholderImage(modifier = Modifier.height(180.dp))
    }
}

@Composable
fun ProductCategoriesSection(onProductClick: (String, String, String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Canvas(modifier = Modifier.size(8.dp)) {
                drawCircle(
                    color = Color.Green,
                    radius = size.minDimension / 2,
                    center = center
                )
            }
            Text(
                text = "PRODUCTS FOR YOU",
                color = Color(0xff141414),
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Product Grid
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(listOf("Carrots", "Cucumbers", "Lettuce", "Pepper")) { product ->
                ProductItem(product, onProductClick) // Pass onProductClick
            }
        }
    }
}

@Composable
fun ProductItem(productName: String, onProductClick: (String, String, String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            // Triggering onProductClick with example data
            onProductClick(productName, "10.00", "product_image_url")
        }
    ) {
        PlaceholderImage(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(
            text = productName,
            color = Color.Black,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun PlaceholderImage(modifier: Modifier) {
    Box(
        modifier = modifier
            .background(Color.Gray)
    )
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Filled.Home, contentDescription = "Home Icon", modifier = Modifier.size(24.dp))
        Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Cart Icon", modifier = Modifier.size(24.dp))
        Icon(imageVector = Icons.Filled.Person, contentDescription = "Profile Icon", modifier = Modifier.size(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HomePageScreenPreview() {
    MaterialTheme {
        HomePageScreen(
            onCartClick = { /* Navigate to CartScreen */ },
            onProductClick = { _, _, _ -> /* Handle Product Click */ }
        )
    }
}
