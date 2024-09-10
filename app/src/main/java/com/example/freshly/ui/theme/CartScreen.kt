import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshly.ui.theme.CartViewModel

@Composable
fun CartScreen(cartViewModel: CartViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Cart",
            style = TextStyle(fontSize = 24.sp)
        )

        if (cartViewModel.cartItems.isEmpty()) {
            Text(
                text = "Cart is empty.",
                style = TextStyle(fontSize = 16.sp)
            )
        } else {
            cartViewModel.cartItems.forEach { item ->
                Text(
                    text = "${item.name} - ${item.quantity} x ${item.price}",
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
    }
}
