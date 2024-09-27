// Import necessary packages
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
import com.example.freshly.ui.theme.CartItem
import com.example.freshly.ui.theme.CartViewModel
import com.example.freshly.ui.theme.PlaceholderImage
import com.example.freshly.ui.theme.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPage(
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit,
    onPlaceOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Get cart items and total price from the ViewModel
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalProductPrice = cartViewModel.getTotalPrice()
    val shippingFee = 15.0  // Example shipping fee
    val totalPrice = totalProductPrice + shippingFee

    // Get user information from UserViewModel
    val userInfo by userViewModel.userInfo.collectAsState()

    val shippingAddress = "${userInfo.firstName} ${userInfo.middleInitial} ${userInfo.lastName}\n${userInfo.address}"
    val paymentMethod = "Cash On Delivery"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar with Back Button and Title
        TopAppBar(
            title = { Text(text = "CHECKOUT", fontSize = 14.sp, color = Color(0xFF141414)) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.eparrowleft),
                        contentDescription = "Back",
                        tint = Color(0xFF141414)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color(0xFF141414),
                navigationIconContentColor = Color(0xFF141414)
            )
        )


        // Shipping Details
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Shipping Details:", fontSize = 20.sp, color = Color(0xFF141414))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = shippingAddress, fontSize = 15.sp, color = Color(0xFF898989))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Edit Info",
                fontSize = 15.sp,
                color = Color(0xFF4E4E4E),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { /* Handle edit action */ }
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFC4C4C4))

        // Cart Items
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

        // Payment Method and Totals
        Column(modifier = Modifier.padding(16.dp)) {
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFC4C4C4))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Payment Method:", fontSize = 15.sp, color = Color(0xFF141414))
                Text(text = paymentMethod, fontSize = 15.sp, color = Color(0xFF898989))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Total Products: ₱${"%.2f".format(totalProductPrice)}", fontSize = 15.sp, color = Color(0xFF898989))
            Text(text = "Shipping Fee: ₱${"%.2f".format(shippingFee)}", fontSize = 15.sp, color = Color(0xFF898989))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total:", fontSize = 20.sp, color = Color(0xFF141414))
                Text(text = "₱${"%.2f".format(totalPrice)}", fontSize = 20.sp, color = Color(0xFF141414))
            }
        }

        // Place Order Button
        Button(
            onClick = onPlaceOrder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128819)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Place Order", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
        // Replace with actual product image
        PlaceholderImage(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = item.name, fontSize = 16.sp, color = Color(0xFF06161C), fontWeight = FontWeight.Medium)
            Text(text = "₱${"%.2f".format(item.price)} x ${item.quantity}", fontSize = 16.sp, color = Color(0xFFFF324B))
        }
        Text(text = "x${item.quantity}", fontSize = 18.sp, color = Color(0xFF06161C), fontWeight = FontWeight.Medium)
    }
}
